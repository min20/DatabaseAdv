#!/bin/bash

mysql -h LEEs-Ubuntu -u shard_master -pdb1004 shard_master < shard_master.sql
mysql -h ShardServer1 -u shard_slave -pdb1004 shard_slave < shard_slave1.sql
mysql -h ShardServer2 -u shard_slave -pdb1004 shard_slave < shard_slave2.sql

