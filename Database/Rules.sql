CREATE RULE transport_delete_prevent AS ON DELETE TO transport DO INSTEAD NOTHING;
CREATE RULE transport_update_prevent AS ON UPDATE TO transport DO INSTEAD NOTHING;

CREATE RULE transport_line_delete_prevent AS ON DELETE TO transport_line DO INSTEAD NOTHING;
CREATE RULE transport_line_update_prevent AS ON UPDATE TO transport_line DO INSTEAD NOTHING;

CREATE RULE inspection_delete_prevent AS ON DELETE TO inspection DO INSTEAD NOTHING;
CREATE RULE inspection_update_prevent AS ON UPDATE TO inspection DO INSTEAD NOTHING;

CREATE RULE city_delete_prevent AS ON DELETE TO city DO INSTEAD NOTHING;
CREATE RULE city_update_prevent AS ON UPDATE TO city DO INSTEAD NOTHING;

CREATE RULE predicted_activity_delete_prevent AS ON DELETE TO predicted_activity DO INSTEAD NOTHING;
CREATE RULE predicted_activity_update_prevent AS ON UPDATE TO predicted_activity DO INSTEAD NOTHING;

CREATE RULE staff_delete_prevent AS ON DELETE TO staff DO INSTEAD NOTHING;

CREATE RULE report_delete_prevent AS ON DELETE TO report DO INSTEAD NOTHING;

CREATE RULE app_admin_delete_prevent AS ON DELETE TO app_admin DO INSTEAD NOTHING;

CREATE RULE message_delete_prevent AS ON DELETE TO message DO INSTEAD NOTHING;

CREATE RULE app_user_delete_prevent AS ON DELETE TO app_user DO INSTEAD NOTHING;

CREATE RULE bicycle_delete_prevent AS ON DELETE TO bicycle DO INSTEAD NOTHING;

CREATE RULE app_transaction_delete_prevent AS ON DELETE TO app_transaction DO INSTEAD NOTHING;

CREATE RULE payment_method_delete_prevent AS ON DELETE TO payment_method DO INSTEAD NOTHING;

CREATE RULE station_delete_prevent AS ON DELETE TO station DO INSTEAD NOTHING;

CREATE RULE settings_delete_prevent AS ON DELETE TO settings DO INSTEAD NOTHING;

CREATE RULE discount_delete_prevent AS ON DELETE TO discount DO INSTEAD NOTHING;

CREATE RULE activity_delete_prevent AS ON DELETE TO activity DO INSTEAD NOTHING;