package com.thesis.webapi;

import com.thesis.webapi.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReportServiceTest.class,
        StationServiceTest.class,
        MessageServiceTest.class,
        DiscountServiceTest.class,
        CityServiceTest.class,
        BicycleServiceTest.class,
        AppUserServiceTest.class,
        AppTransactionServiceTest.class
})
@SpringBootTest
public class WebAPIApplicationTests {

    @Test
    public void contextLoads() {
    }

}
