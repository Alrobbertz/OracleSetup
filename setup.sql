/*
 I CAN PUT THINGS DIRECTLY IN THE DATABASE FROM HERE IF YOU WANT.
 */

select table_name from user_tables;

-- DROP INDEX node_id_index;
-- DROP INDEX edge_id_index;
-- DROP INDEX employee_id_index;
DROP INDEX login_id_index;
-- DROP INDEX manager_id_index;
DROP INDEX permissions_emp_index;
DROP INDEX service_type_index;
DROP INDEX service_assigned_index;

DROP TABLE Edge;
DROP TABLE Permissions;
DROP TABLE Service;
DROP TABLE Login;
DROP TABLE Manage;
DROP TABLE Node;
DROP TABLE Employee;
DROP TABLE Mview;


CREATE TABLE Node(
  nodeID VARCHAR(32),
  longName VARCHAR(255),
  shortName VARCHAR(255),
  coordX2D REAL,
  coordY2D REAL,
  coordX3D REAL,
  coordY3D REAL,
  nodeType CHAR(4),
  building VARCHAR(255),
  floor VARCHAR(10),
  isBeingEdited NUMBER(1),
  CONSTRAINT p_key_node PRIMARY KEY (nodeID));

CREATE TABLE Edge(
  edgeID VARCHAR(65),
  node_id_one VARCHAR(32),
  node_id_two VARCHAR(32),
  CONSTRAINT p_key_edge PRIMARY KEY (edgeID),
  CONSTRAINT f_key_node_one FOREIGN KEY (node_id_one) REFERENCES Node(nodeID),
  CONSTRAINT f_key_node_two FOREIGN KEY (node_id_two) REFERENCES Node(nodeID));

CREATE TABLE Employee(
  employeeID VARCHAR(32),
  first_name VARCHAR(32),
  last_name VARCHAR(32),
  title VARCHAR(32),
  CONSTRAINT p_key_employee PRIMARY KEY (employeeID));

CREATE TABLE Login(
  employeeID VARCHAR(32),
  username   VARCHAR(32),
  password   VARCHAR(32),
  CONSTRAINT p_key_login PRIMARY KEY (employeeID),
  CONSTRAINT f_key_employee FOREIGN KEY (employeeID) REFERENCES Employee(employeeID));

CREATE TABLE Manage(
  employeeID_Manager VARCHAR(32),
  employeeID_Employee  VARCHAR(32),
  CONSTRAINT p_key_manage PRIMARY KEY (employeeID_Manager, employeeID_Employee),
  CONSTRAINT f_key_manage FOREIGN KEY (employeeID_Manager) REFERENCES Employee(employeeID),
  CONSTRAINT f_key_manage2 FOREIGN KEY (employeeID_Employee) REFERENCES  Employee(employeeID));

CREATE TABLE Permissions(
  employeeID VARCHAR(32),
  serviceType VARCHAR(32),
  CONSTRAINT  f_key_permissions FOREIGN KEY  (employeeID) REFERENCES  Employee(employeeID),
  CONSTRAINT  p_key_permissions PRIMARY KEY (employeeID, serviceType));

CREATE TABLE Service(
  serviceID VARCHAR(32),
  kioskID VARCHAR(32),
  request_time DATE,
  serviceType VARCHAR(32),
  destinationNode  VARCHAR(32),
  completed NUMBER(1),
  assignedTo VARCHAR(32),
  hoursToComplete REAL,
  description VARCHAR(512),
  service_name     VARCHAR(32),
  CONSTRAINT p_key_service PRIMARY KEY (serviceID),
  CONSTRAINT f_key_service_node FOREIGN KEY (destinationNode) REFERENCES Node(nodeID));

-- MATERIALIZED VIEW TABLE --
CREATE TABLE MVIEW(
  transactionID RAW(16) DEFAULT sys_guid(),
  table_changed VARCHAR(32),
  update_performed VARCHAR(32),
  pkey_row VARCHAR(100),
  time_executed TIMESTAMP DEFAULT SYSTIMESTAMP,
  CONSTRAINT p_key_mview PRIMARY KEY (transactionID));


------------------------------------ START EDGE TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_edge
  AFTER INSERT
  ON EDGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('edge', 'insert', :new.edgeID);
  END;
/

CREATE OR REPLACE TRIGGER update_edge
  AFTER UPDATE
  ON EDGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('edge', 'update', :new.edgeID);
  END;
/

CREATE OR REPLACE TRIGGER delete_edge
  AFTER DELETE
  ON EDGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('edge', 'delete', :old.edgeID);
  END;
/

------------------------------------ START NODE TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_node
  AFTER INSERT
  ON NODE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('node', 'insert', :new.nodeID);
  END;
/

CREATE OR REPLACE TRIGGER update_node
  AFTER UPDATE
  ON NODE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('node', 'update', :new.nodeID);
  END;
/

CREATE OR REPLACE TRIGGER delete_node
  AFTER DELETE
  ON NODE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('node', 'delete', :old.nodeID);
  END;
/


------------------------------------ START EMPLOYEE TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_employee
  AFTER INSERT
  ON EMPLOYEE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('employee', 'insert', :new.employeeID);
  END;
/

CREATE OR REPLACE TRIGGER update_employee
  AFTER UPDATE
  ON EMPLOYEE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('employee', 'update', :new.employeeID);
  END;
/

CREATE OR REPLACE TRIGGER delete_employee
  AFTER DELETE
  ON EMPLOYEE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('employee', 'delete', :old.employeeID);
  END;
/


------------------------------------ START SERVICE TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_service
  AFTER INSERT
  ON SERVICE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('service', 'insert', :new.serviceID);
  END;
/

CREATE OR REPLACE TRIGGER update_service
  AFTER UPDATE
  ON SERVICE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('service', 'update', :new.serviceID);
  END;
/

CREATE OR REPLACE TRIGGER delete_service
  AFTER DELETE
  ON SERVICE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('service', 'delete', :old.serviceID);
  END;
/

------------------------------------ START MANAGE TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_manage
  AFTER INSERT
  ON MANAGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('manage', 'insert', :new.employeeID_Manager||'_'||:new.employeeID_Employee);
  END;
/

CREATE OR REPLACE TRIGGER update_manage
  AFTER UPDATE
  ON MANAGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('manage', 'update', :new.employeeID_Manager||'_'||:new.employeeID_Employee);
  END;
/

CREATE OR REPLACE TRIGGER delete_manage
  AFTER DELETE
  ON MANAGE
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('manage', 'delete', :old.employeeID_Manager||'_'||:old.employeeID_Employee);
  END;
/

------------------------------------ START PERMISSIONS TRIGGEREDDDD ------------------------------------------
CREATE OR REPLACE TRIGGER insert_permissions
  AFTER INSERT
  ON PERMISSIONS
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('permissions', 'insert', :new.employeeID||'_'||:new.serviceType);
  END;
/

CREATE OR REPLACE TRIGGER update_permissions
  AFTER UPDATE
  ON PERMISSIONS
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('permissions', 'update', :new.employeeID||'_'||:new.serviceType);
  END;
/

CREATE OR REPLACE TRIGGER delete_permissions
  AFTER DELETE
  ON PERMISSIONS
  FOR EACH ROW
  BEGIN
    INSERT INTO MVIEW(table_changed, update_performed, pkey_row) VALUES ('permissions', 'delete', :old.employeeID||'_'||:old.serviceType);
  END;
/


-- CREATE INDEX node_id_index ON Node(nodeID); -- Doesnt work -- PK
-- CREATE INDEX edge_id_index ON Edge(edgeID); -- Doesnt work -- PK
-- CREATE INDEX employee_id_index ON Employee(employeeID); - Doesnt work -- PK
CREATE INDEX login_id_index ON Login(username, password);
-- CREATE INDEX manager_id_index ON Manage(employeeID_Manager, employeeID_Employee); -- Doesnt Work -- PK
CREATE INDEX permissions_emp_index ON Permissions(employeeID);
CREATE INDEX service_type_index ON Service(serviceType);
CREATE INDEX service_assigned_index ON Service(assignedTo);



/* Schemas

*/

