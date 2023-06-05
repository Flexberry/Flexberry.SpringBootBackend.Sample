INSERT INTO customer(primarykey, "name", age) VALUES ( 'e941c8ed-ad19-49be-bac2-230011b05911','Dmitry', 34);
INSERT INTO customer(primarykey, "name", age) VALUES ( '27b4b5a1-f8f1-45b0-ad6d-c52d01204a3d','Santos', 41);
INSERT INTO customer(primarykey, "name", age) VALUES ( '40685f2f-08c9-43e7-aff5-17b94a4322dd','Igor', 30);
INSERT INTO customer(primarykey, "name", age) VALUES ('0aea344c-b379-420f-af8f-1cae390fd5be','Anton', 37);
INSERT INTO customer(primarykey, "name", age) VALUES ('b8b7bfc2-355b-4f89-b389-ec5826f971ea' ,'Andrey', 51);
INSERT INTO customer(primarykey, "name", age) VALUES ( 'bbd7e91d-84d2-4ea4-8bc5-db35f22ab1d5','Emmanuel', 24);
INSERT INTO customer(primarykey, "name", age) VALUES ( '5c57950b-4ad4-4ab5-b85c-e61548ded649','Natasha', 45);
INSERT INTO customer(primarykey, "name", age) VALUES ( '3a8d6837-d4fc-4c51-9031-66690bc8984a','Alexey', 65);
INSERT INTO customer(primarykey, "name", age) VALUES ('0dd3bd9b-e764-4576-85df-e0d3e38c67be' ,'Evgenia', 36);
INSERT INTO customer(primarykey, "name", age) VALUES ( '2ee72fda-85e3-11ed-a1eb-0242ac120002','Anastasia',30);
INSERT INTO customer(primarykey, "name", age) VALUES ( '2ee735e8-85e3-11ed-a1eb-0242ac120002','Nana', 24);
INSERT INTO customer(primarykey, "name", age) VALUES ( '2ee73750-85e3-11ed-a1eb-0242ac120002','Andrey', 28);
INSERT INTO customer(primarykey, "name", age) VALUES ( '2ee738f4-85e3-11ed-a1eb-0242ac120002','Ksenia', 26);
INSERT INTO customer(primarykey, "name", age) VALUES ( '2ee73a34-85e3-11ed-a1eb-0242ac120002','Ruslan', 39);
INSERT INTO customer(primarykey, "name", age) VALUES ( '6fb93d46-85e3-11ed-a1eb-0242ac120002','Egor', 21);
INSERT INTO customer(primarykey, "name", age) VALUES ( '76500df6-85e3-11ed-a1eb-0242ac120002','Roman', 45);
INSERT INTO customer(primarykey, "name", age) VALUES ( '7e5fc496-85e3-11ed-a1eb-0242ac120002','John', 27);
INSERT INTO customer(primarykey, "name", age) VALUES ( '86fe1c4c-85e3-11ed-a1eb-0242ac120002','Kiril', 49);
INSERT INTO customer(primarykey, "name", age) VALUES ( '93897b5a-85e3-11ed-a1eb-0242ac120002','Anna', 18);
INSERT INTO customer(primarykey, "name", age) VALUES ( '98824a2e-85e3-11ed-a1eb-0242ac120002','Angela', 19);

INSERT INTO "comment"(primarykey, commentdate, commenttext, customer_id)
VALUES('ce350952-22b0-4f0b-9cda-c65c78d878ea', '2022-11-08', 'The product was user friendly','e941c8ed-ad19-49be-bac2-230011b05911');
INSERT INTO "comment"(primarykey,commentdate,commenttext, customer_id) 
VALUES('d3552871-a980-48f1-96b2-7a58719f2453', '2022-08-25', ' It is free service', '40685f2f-08c9-43e7-aff5-17b94a4322dd');
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id) 
VALUES('9fa1e28a-270a-43dc-b618-51b1074c5be8', '2022-08-07', 'I liked the customer service','b8b7bfc2-355b-4f89-b389-ec5826f971ea');
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id)
VALUES('b5e70876-546a-42ea-85d6-0bc14ed5178a', '2022-08-18', 'Delivery was on time', '98824a2e-85e3-11ed-a1eb-0242ac120002');
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id)
VALUES('ecb238b8-b2cb-4901-88ab-cec3cb98f041', '2022-02-11', 'Team are experts','86fe1c4c-85e3-11ed-a1eb-0242ac120002');
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id)
VALUES('4550aa8b-e03e-4912-9630-5057357a34fc', '2022-05-05', 'Issues with installations was solved','76500df6-85e3-11ed-a1eb-0242ac120002');	   
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id)
VALUES('d2622287-abd5-4c6f-87a2-259b024dcb95', '2022-06-21', 'flexberry ORM was useful', '2ee738f4-85e3-11ed-a1eb-0242ac120002');
INSERT INTO "comment"(primarykey,commentdate,commenttext, customer_id )
VALUES('5cee0f1f-cbe5-4f45-a082-306da0045aa6', '2022-02-14', 'onboarding', '0dd3bd9b-e764-4576-85df-e0d3e38c67be');
INSERT INTO "comment"(primarykey,commentdate,commenttext,customer_id)
VALUES('6a9605e3-13b6-420e-ae24-6facc23797a0', '2022-07-17', 'no reply to emails.', '2ee735e8-85e3-11ed-a1eb-0242ac120002' );
INSERT INTO "comment"(primarykey, commentdate, commenttext,customer_id)
VALUES('bab97748-c0fd-43ac-af07-c665679745ab', '2022-12-20', 'I need a discount', '27b4b5a1-f8f1-45b0-ad6d-c52d01204a3d');

