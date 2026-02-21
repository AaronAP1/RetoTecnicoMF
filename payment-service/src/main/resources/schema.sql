create table if not exists payment_authorizations (
    authorization_id varchar(50) primary key,
    request_id varchar(60) not null unique,
    status varchar(20) not null,
    amount decimal(12,2) not null,
    currency varchar(3) not null,
    decline_reason varchar(50),
    created_at timestamp not null
    );
