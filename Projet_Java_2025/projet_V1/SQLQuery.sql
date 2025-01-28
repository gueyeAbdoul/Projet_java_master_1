
DROP TABLE IF EXISTS client cascade;
DROP TABLE IF EXISTS produit cascade;
DROP TABLE IF EXISTS commande cascade;
DROP TABLE IF EXISTS panier cascade;
DROP TABLE IF EXISTS produit_panier cascade;
DROP TABLE IF EXISTS facture cascade;

CREATE TABLE client (
                        idClient SERIAL PRIMARY KEY,
                        nomClient VARCHAR(50) NOT NULL,
                        prenomClient VARCHAR(50) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        adresse TEXT,
                        telephone VARCHAR(15),
                        login VARCHAR(50) UNIQUE NOT NULL,
                        motdepasse VARCHAR(255) NOT NULL
);

INSERT INTO client(nomClient, prenomClient, email, adresse, telephone, login, motdepasse)
VALUES('Abdoul', 'GUEYE', 'abdoul@gmail.com', '11 Av. de la Convention', '0484383354', 'abdoul', 'gueye'),
      ('Amadou', 'SOW', 'sow@gmail.com', '11 Av. de la Convention', '9439393245', 'amadou', 'sow');

CREATE TABLE produit (
                         idProduit SERIAL PRIMARY KEY,
                         nomProduit VARCHAR(100) NOT NULL,
                         descriptionProduit TEXT,
                         prixProduit FLOAT NOT NULL,
                         quantiteStock INT NOT NULL,
                         categorie VARCHAR(50) NOT NULL,
                         imageProduit VARCHAR(255)
);

CREATE TABLE panier (
                        idPanier SERIAL PRIMARY KEY,
                        idClient INT NOT NULL REFERENCES client(idClient) ON DELETE CASCADE,
                        etatPanier VARCHAR(50),
                        datePanier DATE NOT NULL DEFAULT CURRENT_DATE,
                        montantPanier float
);

CREATE TABLE produit_panier (
                                  idpanier INT NOT NULL REFERENCES panier(idpanier) ON DELETE CASCADE,
                                  idProduit INT NOT NULL REFERENCES produit(idProduit) ON DELETE CASCADE,
                                  quantiteproduit INT NOT NULL,
                                  prixProduitPanier float,
                                  PRIMARY KEY (idpanier, idProduit)
);

/*ALTER TABLE produit_panier ADD COLUMN prixProduitPanier float;*/

CREATE TABLE commande (
                          idCommande SERIAL PRIMARY KEY,
                          idClient INT NOT NULL REFERENCES client(idClient) ON DELETE CASCADE,
                          idpanier INT NOT NULL REFERENCES panier(idpanier) ON DELETE CASCADE,
                          etatCommande VARCHAR(20) DEFAULT 'en cours',
                          montantCommande float,
                          dateCommande DATE NOT NULL DEFAULT CURRENT_DATE
);

/*ALTER TABLE produit ADD COLUMN imageProduit VARCHAR(255);*/

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Samsung SM-A057', 'Smartphone', 'Samsung SM-A057 Galaxy A05s Dual SIM 4GB RAM 64GB Black EU', 130.99, 12,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/SMA057.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Xiaomi Redmi Note 13', 'Smartphone', 'Xiaomi Redmi Note 13 4G Dual SIM 8GB RAM 128GB Midnight Black EU', 155.00, 15,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/Xiaomi.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('OUKITEL C36', 'Téléphone Portable', 'OUKITEL C36 Téléphone Portable Débloqué, 12Go+128Go/1T Extensible, 5150mAh 6.56" HD+ Display Android 13 Smartphone Pas Cher, Octa Core Processor, 13MP', 96.00, 5,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/Oukitel.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Acer Aspire', 'Ordinateur portable', 'Acer Aspire 1 A115-32-C3AK Ordinateur Portable 15,6'''' Full HD, PC Portable (Intel Celeron N4500, RAM 4 Go, 128 Go eMMC, Intel UHD Graphics, Windows', 249.99, 20,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/acer.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Asus Vivobook', 'Ordinateur portable', 'Asus Vivobook 15 R1502QA-EJ373W 15.6 pouces FHD PC Portable (Processeur AMD Ryzen 7 5800H , 16Go RAM DDR4, 1To SSD NVMe PCIe 4.0, Windows 11 Home) - Clavier', 548.86, 15,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/asus.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Lenovo', 'Ordinateur portable', 'Lenovo (FullHD 15,6 Zoll Ordinateur Portable (Intel® Quad N5100 4x2.80 GHz, 16Go DDR4, 1000 Go SSD, Intel™ UHD, HDMI, BT, USB 3.0, Webcam, WLAN, Windows 11', 369.00, 18,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/Lenovo.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('AOC 2024 Gaming', 'Ordinateur portable', 'AOC 2024 Gaming Ordinateur Portable Boîtier en Métal et Rétroéclairé, AMD Ryzen 7 5825U(8C/16T, jusqu''à 4,50 GHz,15W) PC Portable Gamer 16,10 Pouces', 499.99, 6,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/AOC.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Apple MacBook Air', 'Ordinateur portable', 'Apple MacBook Air Portable avec Puce M2 : écran Liquid Retina de 13,6 Pouces, 16 Go de RAM, 256 Go de Stockage SSD, Clavier rétroéclairé, caméra FaceTime HD', 990.86, 15,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/Apple1.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('HP EliteBook', 'Ordinateur portable', 'HP EliteBook 845 G7 PC Portable + Sacoche HP Offerte, UltraBook 14" Full HD 1920x1080, Ryzen 5 Pro 4650U 6 Cores Up to 4 GHz, RAM 16 Go, SSD 512 Go', 699.00, 10,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/hp1.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('HP EliteBook', 'Ordinateur portable', 'HP EliteBook 860 G10 (7L7U4ET) (Silber, Windows 11 Pro 64-Bit, 512 GB SSD)', 2211.99, 2,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/hp2.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('Apple iphone 16', 'SMARTPHONE', 'Apple iPhone 16 Pro Max (256 Go) - Titane Blanc', 1019.86, 15,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/iphone16.jpg');

INSERT INTO produit(nomProduit, categorie, descriptionProduit, prixProduit, quantiteStock, imageProduit)
VALUES ('APPLE iPhone 15', 'SMARTPHONE', 'Apple iPhone 15 Pro (1 to) - Titane Bleu', 969.00, 3,
        'C:/Users/hp/OneDrive - Université Paris-Dauphine/Bureau/DAUPHINE_2025/Programmation Objet avancée/ProjetJava2024/projet_V1/src/interfaceGraphique/gestionproduit/telephone/iphone15.jpg');

CREATE TABLE facture (
                         idFacture SERIAL PRIMARY KEY,
                         idCommande INT NOT NULL REFERENCES commande(idCommande) ON DELETE CASCADE,
                         montantFacture FLOAT NOT NULL,
                         dateFacture DATE NOT NULL DEFAULT CURRENT_DATE
);
