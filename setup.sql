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

