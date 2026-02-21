create table if not exists policies (
    policy_id varchar(40) primary key,
    request_id varchar(60) not null unique,
    status varchar(30) not null,
    product_type varchar(20) not null,
    premium_amount decimal(12,2) not null,
    premium_currency varchar(3) not null,
    payment_request_id varchar(60) not null,
    payment_authorization_id varchar(40),
    created_at timestamp not null,
    issued_at timestamp
    );