CREATE TABLE `user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_at` DATETIME NOT NULL,
	`updated_at` DATETIME NOT NULL,
	`email` VARCHAR(40) NULL DEFAULT NULL,
	`name` VARCHAR(40) NULL DEFAULT NULL,
	`password` VARCHAR(100) NULL DEFAULT NULL,
	`username` VARCHAR(15) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
	UNIQUE INDEX `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
);

CREATE TABLE `roles` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(60) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
);

CREATE TABLE `user_roles` (
	`user_id` BIGINT(20) NOT NULL,
	`role_id` BIGINT(20) NOT NULL,
	PRIMARY KEY (`user_id`, `role_id`),
	INDEX `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
	CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
	CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);

create table polls
(
	id                   bigint auto_increment primary key,
	created_at           datetime     not null,
	updated_at           datetime     not null,
	created_by           bigint       null,
	updated_by           bigint       null,
	expiration_date_time datetime     not null,
	question             varchar(140) null
);

create table choices
(
	id      bigint auto_increment
		primary key,
	text    varchar(40) null,
	poll_id bigint      not null
);

CREATE TABLE votes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    choice_id  BIGINT   NOT NULL,
    poll_id    BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    constraint UK8um9h2wxsdjrgx3rjjwvny676
        unique (poll_id, user_id),
    constraint FK7trt3uyihr4g13hva9d31puxg
        foreign key (poll_id) references polls (id),
    constraint FKomskymhxde3qq9mcukyp1puio
        foreign key (choice_id) references choices (id),
    constraint FKqmhupfdite8fxh3dw1bvc2pti
        foreign key (user_id) references user (id)
);




