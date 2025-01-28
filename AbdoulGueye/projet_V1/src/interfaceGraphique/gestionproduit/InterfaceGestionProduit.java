package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterfaceGestionProduit extends JPanel {
    private int clientId; // ID du client connecté
    private InterfaceGestionPanier gestionPanierPanel;
    private JPanel produitPanel; // Panel pour afficher les produits

    public InterfaceGestionProduit(int clientId, InterfaceGestionPanier gestionPanierPanel) {
        this.clientId = clientId;
        this.gestionPanierPanel = gestionPanierPanel;

        setLayout(new BorderLayout());

        // Barre de recherche
        JPanel recherchePanel = new JPanel(new BorderLayout());
        JTextField rechecrheProd = new JTextField();
        rechecrheProd.setPreferredSize(new Dimension(50, 25));
        JButton RechercheButton = new JButton("Rechercher");

        recherchePanel.add(rechecrheProd, BorderLayout.CENTER);
        recherchePanel.add(RechercheButton, BorderLayout.EAST);
        add(recherchePanel, BorderLayout.NORTH);

        // Panel des produits
        produitPanel = new JPanel();
        produitPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 produits par ligne
        JScrollPane scrollPane = new JScrollPane(produitPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Charger tous les produits au départ
        chargerProduits(null);

        // Action du bouton de recherche
        RechercheButton.addActionListener(e -> {
            String rechercheText = rechecrheProd.getText().trim();
            chargerProduits(rechercheText);
        });
    }

    private void chargerProduits(String rechercheText) {
        produitPanel.removeAll(); // Effacer les produits existants

        String query = "SELECT * FROM produit";
        if (rechercheText != null && !rechercheText.isEmpty()) {
            query += " WHERE LOWER(nomProduit) LIKE ? OR LOWER(categorie) LIKE ?";
        }

        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (rechercheText != null && !rechercheText.isEmpty()) {
                String searchPattern = "%" + rechercheText.toLowerCase() + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nom = rs.getString("nomProduit");
                    String description = rs.getString("descriptionProduit");
                    float prix = rs.getFloat("prixProduit");
                    int quantite = rs.getInt("quantiteStock");
                    String categorie = rs.getString("categorie");
                    String cheminImage = rs.getString("imageProduit");

                    JPanel productCard = createProductCard(nom, description, prix, quantite, categorie, cheminImage);
                    produitPanel.add(productCard);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des produits.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        produitPanel.revalidate();
        produitPanel.repaint();
    }

    private JPanel createProductCard(String nom, String description, float prix, int quantite, String categorie, String cheminImage) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        card.setPreferredSize(new Dimension(200, 300)); // Taille uniforme des cartes

        // Charger l'image
        ImageIcon productImage;
        try {
            productImage = new ImageIcon(cheminImage);
            Image scaledImage = productImage.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            productImage = new ImageIcon(scaledImage);
        } catch (Exception e) {
            productImage = new ImageIcon(); // Image vide si erreur
        }

        JLabel imageLabel = new JLabel(productImage, SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Ajouter les informations du produit
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel nomLabel = new JLabel(" Nom: " + nom, SwingConstants.CENTER);
        JLabel categorieLabel = new JLabel(" Categorie: " + categorie, SwingConstants.CENTER);
        JLabel descriptionLabel = new JLabel(" Description: " + description, SwingConstants.CENTER);
        JLabel prixLabel = new JLabel(" Prix: " + prix + "€", SwingConstants.CENTER);
        JLabel quantiteLabel = new JLabel(" Quantité disponible: " + quantite, SwingConstants.CENTER);

        infoPanel.add(nomLabel);
        infoPanel.add(categorieLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(prixLabel);
        infoPanel.add(quantiteLabel);

        // Ajouter bouton "Ajouter à la commande"
        JButton addButton = new JButton("Ajouter au panier");
        addButton.addActionListener(e -> {
            try {
                gestionPanierPanel.ajouterProduit(nom, prix);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        infoPanel.add(addButton);

        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }
}
