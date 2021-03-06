CREATE TABLE `openerp_debtor_list` (
            `id` int(11) NOT NULL auto_increment,
            `invoice_id` varchar(255) NOT NULL,
            `patient_id` char(38) NOT NULL,
            `patient_name` varchar(255) NOT NULL,
            `product_name` varchar(255) NOT NULL,
            `chargeable_amount` double NOT NULL,
            `default_quantity` int(255) NOT NULL,
            `comment` varchar(1000),
            `identifier_type` int(11) NOT NULL default '0',
            `creator` int(11) NOT NULL default '0',
            `date_created` datetime NOT NULL default '0000-00-00 00:00:00',
            `changed_by` int(11) default NULL,
            `date_changed` datetime default NULL,
            `retired` tinyint(1) NOT NULL default 0,
            `retired_by` int(11) default NULL,
            `date_retired` datetime default NULL,
            `retire_reason` varchar(255) default NULL,
            PRIMARY KEY  (`id`)
            );