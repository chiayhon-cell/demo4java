create table `STREAM`
(
    `id`         bigint      not null primary key,
    `operation`  varchar(64) not null default '',
    `createTime` datetime
)