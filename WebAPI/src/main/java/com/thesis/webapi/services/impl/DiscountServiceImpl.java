package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.*;
import com.thesis.webapi.repositories.*;
import com.thesis.webapi.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    private final CityRepository cityRepository;

    private final TransportRepository transportRepository;

    private final TransportLineRepository transportLineRepository;

    private final StationRepository stationRepository;

    private final PredictedActivityRepository predictedActivityRepository;

    private final StaffRepository staffRepository;

    private final SettingsRepository settingsRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository,
                               CityRepository cityRepository,
                               TransportRepository transportRepository,
                               TransportLineRepository transportLineRepository,
                               StationRepository stationRepository,
                               PredictedActivityRepository predictedActivityRepository,
                               StaffRepository staffRepository,
                               SettingsRepository settingsRepository) {
        this.discountRepository = discountRepository;
        this.cityRepository = cityRepository;
        this.transportRepository = transportRepository;
        this.transportLineRepository = transportLineRepository;
        this.stationRepository = stationRepository;
        this.predictedActivityRepository = predictedActivityRepository;
        this.staffRepository = staffRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public ResponseEntity<List<Discount>> getDiscountsByStationAndTime(UUID stationId) {
        Date date = new Date();
        return new ResponseEntity<>(discountRepository.getDiscountsByStationAndTime(stationId, date), HttpStatus.OK);
    }

    @Override
    public void bringStationBicycleNumbersToPredictedValues() {
        Date date = new Date();
        List<City> cityList = cityRepository.findAll();
        int hour = date.toInstant().atZone(ZoneId.systemDefault()).getHour();
        if(hour >= 6 && hour < 21) {
            List<Staff> staffList = staffRepository.getAvailableDriversForTransport();
            for (City city : cityList) {
                System.out.println("Attempting to bring station bicycle numbers to predicted values for the city " + city.getName() + "...");
                Settings settings = settingsRepository.getSettingsByCityId(city.getCityId());
                List<Station> stationList = stationRepository.getStationsByCityId(city.getCityId());
                HashMap<Station, Integer> positiveDifference = new HashMap<>();
                HashMap<Station, Integer> negativeDifference = new HashMap<>();
                for (Station station : stationList) {
                    PredictedActivity predictedActivity =
                            predictedActivityRepository.getPredictedActivityByStationIdAndDayAndHour(station.getId(), date, hour + 1);
                    if(predictedActivity != null) {
                        int difference = station.getCurrentCapacity() - predictedActivity.getNumberOfBicycles();
                        if(Math.abs(difference) > (int)(station.getMaxCapacity()*(7.5f / 100.0f))) {
                            if(difference > 0) {
                                positiveDifference.put(station, difference);
                            }
                            else {
                                negativeDifference.put(station, difference);
                            }
                        }
                    }
                    else {
                        System.out.println("Predicted activity wasn't generated for station " +
                                station.getName() + " of city " + city.getName());
                    }
                }
                for(Map.Entry<Station, Integer> positivePair : positiveDifference.entrySet()) {
                    int maxValueForDiscount = (int)(positivePair.getKey().getMaxCapacity()*(25.0f / 100.0f));
                    if(positivePair.getValue() <= maxValueForDiscount) {
                        for(Map.Entry<Station, Integer> negativePair : negativeDifference.entrySet()) {
                            int minValueForDiscount = -(int)(negativePair.getKey().getMaxCapacity()*(25.0f / 100.0f));
                            if(positivePair.getValue() > 0 && negativePair.getValue() >= minValueForDiscount && negativePair.getValue() < 0) {
                                int numberOfDiscounts = Math.min(positivePair.getValue(), -negativePair.getValue());
                                Discount discount = new Discount(positivePair.getKey().getId(), negativePair.getKey().getId(), numberOfDiscounts,
                                        settings.getDiscountValue(), date);
                                discountRepository.save(discount);
                                positivePair.setValue(positivePair.getValue() - numberOfDiscounts);
                                negativePair.setValue(negativePair.getValue() + numberOfDiscounts);
                            }
                        }
                    }
                    else {
                        //TO DO TRANSPORTS + VERIFICAT CE AM SCRIS SUS LA DISCOUNT + PUS IN METODA NOUA
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 40 * * * *")
    public void onScheduleCallBringStationBicycleNumbersToPredictedValues() {
        bringStationBicycleNumbersToPredictedValues();
    }
}
