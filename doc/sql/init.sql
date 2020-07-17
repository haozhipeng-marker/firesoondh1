create tablespace firesoondh
logging
datafile '/opt/oracle/oradata/hdctest/firesoondh.dbf'
size 1G
autoextend on
next 100m maxsize UNLIMITED
extent management local;

create user fsdh identified by fsdh default tablespace firesoondh;

grant resource,connect,dba to fsdh

grant all privileges to fsdh