package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InterfaceFacture extends JFrame {

    public InterfaceFacture(int idFacture) {
        setTitle("Facture de la commande");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau pour l'en-tête (titre et informations du client)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10)); // Grande marge pour le titre

        // Titre de la facture
        JLabel titleLabel = new JLabel("Facture N°" + idFacture, JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Panneau pour les informations du client
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // Marges internes
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTable produitTable = null;
        float totalHT = 0; // Variable pour calculer le montant total HT

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            // Requête pour récupérer les informations de la facture
            String queryFacture = """
                    SELECT f.idFacture, f.dateFacture, f.montantFacture, 
                           c.nomClient, c.prenomClient, c.email, c.telephone, c.adresse, 
                           cmd.dateCommande, cmd.etatCommande, 
                           p.nomProduit, p.categorie, 
                           pp.quantiteProduit, pp.prixProduitPanier
                    FROM Facture f
                    JOIN Commande cmd ON f.idCommande = cmd.idCommande
                    JOIN Client c ON cmd.idClient = c.idClient
                    JOIN produit_panier pp ON cmd.idPanier = pp.idPanier
                    JOIN Produit p ON pp.idProduit = p.idProduit
                    WHERE f.idFacture = ?
                """;
            try (PreparedStatement stmt = conn.prepareStatement(queryFacture)) {
                stmt.setInt(1, idFacture);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Ajouter les informations générales sur la facture
                        infoPanel.add(createLabel("Date de Facture: " + rs.getDate("dateFacture"), 16));
                        infoPanel.add(createLabel("Nom du Client: " + rs.getString("prenomClient") + " " + rs.getString("nomClient"), 16));
                        infoPanel.add(createLabel("Email: " + rs.getString("email"), 16));
                        infoPanel.add(createLabel("Téléphone: " + rs.getString("telephone"), 16));
                        infoPanel.add(createLabel("Adresse: " + rs.getString("adresse"), 16));
                        infoPanel.add(createLabel("Date de Commande: " + rs.getDate("dateCommande"), 16));
                        infoPanel.add(createLabel("Etat de la Commande: " + rs.getString("etatCommande"), 16));
                        infoPanel.add(createLabel("Montant Total: " + rs.getFloat("montantFacture") + " €", 16));

                        // Espacement
                        infoPanel.add(Box.createVerticalStrut(20));

                        // Configuration du tableau des produits
                        String[] columns = {"Produit", "Catégorie", "Quantité", "Prix Unitaire (€)", "Total (€)"};
                        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

                        do {
                            String nomProduit = rs.getString("nomProduit");
                            String categorie = rs.getString("categorie");
                            int quantite = rs.getInt("quantiteProduit");
                            float prixUnitaire = rs.getFloat("prixProduitPanier");
                            float total = quantite * prixUnitaire;
                            totalHT += total; // Ajouter au total HT

                            Object[] rowData = {nomProduit, categorie, quantite, prixUnitaire, total};
                            tableModel.addRow(rowData);
                        } while (rs.next());

                        produitTable = new JTable(tableModel);
                        produitTable.setFillsViewportHeight(true);
                        produitTable.setRowHeight(30);
                        produitTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                        produitTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        produitTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune facture trouvée pour l'ID donné.");
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de la facture : " + e.getMessage());
        }

        // Ajouter les composants au centre
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        if (produitTable != null) {

            JScrollPane scrollPane = new JScrollPane(produitTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Ajout de marges pour le tableau
            scrollPane.setPreferredSize(new Dimension(750, 200)); // Ajuster la hauteur du tableau pour laisser de l'espace pour les totaux
            add(scrollPane, BorderLayout.CENTER);
        }

        // Créer un panneau pour afficher les totaux
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajouter des marges

        // Calculs et affichage du total HT, TVA et Total TTC
        float tva = totalHT * 0.20f; // TVA à 20%
        float totalTTC = totalHT + tva;

        // Ajout des labels de Total HT, TVA et Total TTC
        totalPanel.add(createLabel("Total HT: " + totalHT + " €", 16));
        totalPanel.add(Box.createVerticalStrut(10));  // Ajouter de l'espace entre les lignes
        totalPanel.add(createLabel("TVA (20%): " + tva + " €", 16));
        totalPanel.add(Box.createVerticalStrut(10));  // Ajouter de l'espace entre les lignes
        totalPanel.add(createLabel("Total TTC: " + totalTTC + " €", 16));

        add(totalPanel, BorderLayout.PAGE_END);

        // Panneau pour le bouton Fermer
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton fermerBtn = new JButton("Fermer");
        fermerBtn.addActionListener(e -> dispose());
        buttonPanel.add(fermerBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Méthode pour créer un JLabel avec une taille de police personnalisée
    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        return label;
    }
}
