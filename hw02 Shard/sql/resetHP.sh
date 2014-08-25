#!/bin/bash

mysql -h ShardServer1 -u shard_slave -pdb1004 shard_slave -e "UPDATE galaxy SET hp = 100000"
mysql -h ShardServer2 -u shard_slave -pdb1004 shard_slave -e "UPDATE galaxy SET hp = 100000"

