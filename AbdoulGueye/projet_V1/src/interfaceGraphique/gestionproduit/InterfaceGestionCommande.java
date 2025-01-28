package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;
import interfaceGraphique.InterfacePrincipale;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceGestionCommande extends JPanel {

    private int clientId;
    private JLabel nomLabel, prenomLabel, emailLabel, telephoneLabel, adresseLabel;

    private JButton validerPanierButton, supprimerPanierButton;
    private int commandeEnCoursId;

    public int getCommandeEnCoursId() throws SQLException {

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String selectQuery = """
                    SELECT idCommande FROM commande WHERE idClient = ? AND etatcommande = 'en cours'
                    """;
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                commandeEnCoursId = rs.getInt("idCommande");
            } else {
                commandeEnCoursId = 0;
            }
        }
        return commandeEnCoursId;
    }

    public InterfaceGestionCommande(int clientId) throws SQLException {
        this.clientId = clientId;
        InterfacePrincipale interPr = new InterfacePrincipale();

        // Panneau pour le bouton Retour
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> {

            interPr.onClientLoggedIn(clientId);

            // Ferme la fenêtre courant
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
        });
        topPanel.add(retourButton, BorderLayout.WEST);

        // Panneau principal pour le contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("Gestion de la commande N°" + getCommandeEnCoursId());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        // Ajout des labels d'information client
        nomLabel = createInfoLabel("Nom", gbc, 1, contentPanel);
        prenomLabel = createInfoLabel("Prénom", gbc, 3, contentPanel);
        emailLabel = createInfoLabel("Email", gbc, 5, contentPanel);
        telephoneLabel = createInfoLabel("Téléphone", gbc, 7, contentPanel);
        adresseLabel = createInfoLabel("Adresse", gbc, 9, contentPanel);

        // Affichage du panier en cours
        afficherCommandeEnCours(gbc, contentPanel);

        // Boutons de gestion
        JPanel buttonPanel = new JPanel(new FlowLayout());
        validerPanierButton = new JButton("Passer votre commande");
        supprimerPanierButton = new JButton("Annuler la commande");
        buttonPanel.add(validerPanierButton);
        buttonPanel.add(supprimerPanierButton);

        // Ajouter le panneau des boutons
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbc);

        // Ajouter les listeners pour les boutons
        validerPanierButton.addActionListener(e -> {
            try {
                validerCommande();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Action pour le bouton Annuler la commande
        supprimerPanierButton.addActionListener(e -> {
            try {

                supprimerCommande();

                // Appeler onClientLoggedIn pour revenir à l'interface principale
                interPr.onClientLoggedIn(clientId);

                // Fermer la fenêtre actuelle
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    frame.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Ajouter le panneau principal au JPanel
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        chargerInformationsClient();
    }

    private JLabel createInfoLabel(String label, GridBagConstraints gbc, int row, JPanel parent) {
        JLabel jLabel = new JLabel(label + ": ");
        jLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        parent.add(jLabel, gbc);

        JLabel infoLabel = new JLabel();
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        parent.add(infoLabel, gbc);

        return infoLabel;
    }

    private void chargerInformationsClient() {
        String sql = "SELECT nomClient, prenomClient, adresse, email, telephone FROM client WHERE idClient = ?";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                nomLabel.setText(rs.getString("nomClient"));
                prenomLabel.setText(rs.getString("prenomClient"));
                emailLabel.setText(rs.getString("email"));
                telephoneLabel.setText(rs.getString("telephone"));
                adresseLabel.setText(rs.getString("adresse"));
            } else {
                JOptionPane.showMessageDialog(this, "Aucune information trouvée pour ce client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des informations client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherCommandeEnCours(GridBagConstraints gbc, JPanel parent) {
        JPanel commandePanel = new JPanel();
        commandePanel.setLayout(new BoxLayout(commandePanel, BoxLayout.Y_AXIS));
        commandePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int idCommandeEnCours = 0;

        try {
            // Étape 1 : Récupérer l'ID de la commande en cours
            idCommandeEnCours = getCommandeEnCoursId();
            System.out.println("idCommandeEnCours" + idCommandeEnCours);

            if (idCommandeEnCours == 0) {
                // Si aucune commande en cours, afficher uniquement l'image et le message
                JPanel panneauVide = new JPanel(new BorderLayout());

                // Redimensionner l'image
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/interfaceGraphique/gestionproduit/panier.png"));
                Image image = imageIcon.getImage().getScaledInstance(700, 400, Image.SCALE_SMOOTH);
                JLabel imageVide = new JLabel(new ImageIcon(image));
                imageVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer l'image horizontalement

                // Ajouter la phrase
                JLabel texteVide = new JLabel("Aucune commande en cours");
                texteVide.setForeground(Color.RED);
                texteVide.setFont(new Font("Arial", Font.BOLD, 30));
                texteVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte

                panneauVide.add(imageVide, BorderLayout.CENTER);
                panneauVide.add(texteVide, BorderLayout.SOUTH);

                commandePanel.add(panneauVide, BorderLayout.CENTER);
            } else {
                // Étape 2 : Récupérer et afficher les produits associés à la commande
                boolean commandeVide = true; // Détecter si le panier est vide

                String produitsQuery = """
            SELECT produit.imageProduit, produit.nomProduit, produit.categorie, 
                   produit.descriptionproduit, produit.prixproduit 
            FROM commande
            INNER JOIN panier ON commande.idPanier = panier.idPanier
            INNER JOIN produit_panier ON panier.idPanier = produit_panier.idPanier
            INNER JOIN produit ON produit_panier.idProduit = produit.idProduit
            WHERE commande.idCommande = ? AND commande.etatcommande='en cours'
            """;

                try (PreparedStatement produitsStmt = ConnexionBasedeDonnee.getConnection().prepareStatement(produitsQuery)) {
                    produitsStmt.setInt(1, idCommandeEnCours);
                    ResultSet rsProduits = produitsStmt.executeQuery();

                    while (rsProduits.next()) {
                        commandeVide = false;
                        // Récupérer les données du produit
                        String imageProduit = rsProduits.getString("imageProduit");
                        String nomProduit = rsProduits.getString("nomProduit");
                        String nomCategorie = rsProduits.getString("categorie");
                        String description = rsProduits.getString("descriptionproduit");
                        double prix = rsProduits.getDouble("prixproduit");

                        // Créer un panneau pour afficher les détails du produit
                        JPanel produitPanel = new JPanel(new BorderLayout());
                        produitPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        produitPanel.setPreferredSize(new Dimension(800, 120));

                        // Image du produit
                        JLabel produitImage = new JLabel();
                        ImageIcon icon = new ImageIcon(imageProduit);
                        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        produitImage.setIcon(new ImageIcon(img));
                        produitImage.setHorizontalAlignment(SwingConstants.CENTER);

                        produitPanel.add(produitImage, BorderLayout.WEST);

                        JPanel detailsPanel = new JPanel();
                        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        detailsPanel.add(new JLabel("Nom: " + nomProduit));
                        detailsPanel.add(new JLabel("Catégorie: " + nomCategorie));
                        detailsPanel.add(new JLabel("Description: " + description));
                        detailsPanel.add(new JLabel("Prix: " + prix + " €"));

                        produitPanel.add(detailsPanel, BorderLayout.CENTER);
                        commandePanel.add(produitPanel);

                        commandePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }

                // Étape 3 : Récupérer et afficher les informations de la commande
                String etatCommande = null;
                double montantCommande = 0.0;
                Date dateCommande = null;

                String commandeInfoQuery = """
            SELECT etatCommande, montantCommande, dateCommande
            FROM commande
            WHERE idCommande = ?
            """;

                try (Connection conn = ConnexionBasedeDonnee.getConnection();
                     PreparedStatement infoStmt = conn.prepareStatement(commandeInfoQuery)) {

                    infoStmt.setInt(1, idCommandeEnCours);
                    ResultSet rsInfo = infoStmt.executeQuery();

                    if (rsInfo.next()) {
                        etatCommande = rsInfo.getString("etatCommande");
                        montantCommande = rsInfo.getDouble("montantCommande");
                        dateCommande = rsInfo.getDate("dateCommande");
                    }
                }

                if (etatCommande == null || dateCommande == null) {
                    JOptionPane.showMessageDialog(this, "Impossible de récupérer les informations de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JPanel infoPanel = new JPanel(new GridLayout(1, 3));
                infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                infoPanel.setPreferredSize(new Dimension(800, 40));

                infoPanel.add(new JLabel("État : " + etatCommande));
                infoPanel.add(new JLabel("Montant : " + montantCommande + " €"));
                infoPanel.add(new JLabel("Date : " + dateCommande));

                commandePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                commandePanel.add(infoPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        parent.add(commandePanel, gbc);
    }

    public void validerCommande() throws SQLException {
        int idpa = getCommandeEnCoursId();
        if (clientId == 0 || idpa == 0) {
            JOptionPane.showMessageDialog(null, "Aucune commande en cours");
            return;
        }
        System.out.println(idpa);
        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            conn.setAutoCommit(false); // Démarrer une transaction

            // Valider la commande
            String query = "UPDATE Commande SET etatcommande = 'Validée' WHERE idcommande = ? AND idclient = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idpa);
                stmt.setInt(2, clientId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Créer une facture
                    String insertFactureQuery = "INSERT INTO Facture (idCommande, montantFacture) SELECT idCommande, montantCommande FROM Commande WHERE idCommande = ?";
                    try (PreparedStatement factureStmt = conn.prepareStatement(insertFactureQuery, Statement.RETURN_GENERATED_KEYS)) {
                        factureStmt.setInt(1, idpa);
                        factureStmt.executeUpdate();

                        ResultSet rs = factureStmt.getGeneratedKeys();
                        if (rs.next()) {
                            int idFacture = rs.getInt(1);
                            conn.commit(); // Valider la transaction
                            JOptionPane.showMessageDialog(null, "Commande validée et facture générée avec succès !");

                            // Mettre à jour l'affichage
                            mettreAJourAffichage();
                            // Afficher la facture
                            new InterfaceFacture(idFacture);
                        } else {
                            conn.rollback(); // Annuler la transaction en cas d'échec
                            JOptionPane.showMessageDialog(null, "Erreur lors de la génération de la facture.");
                        }
                    }
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Impossible de valider la commande.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la validation : " + e.getMessage());
        }
    }

    public void supprimerCommande() throws SQLException {
        int idpa = getCommandeEnCoursId();
        if (clientId == 0 || idpa == 0) {
            System.out.println("idpa : " + idpa + " || idclient : " + clientId);
            JOptionPane.showMessageDialog(null, "Aucune commande en cours");
            return;
        }

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String query = "DELETE FROM commande WHERE idcommande = ? AND idclient = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idpa);
                stmt.setInt(2, clientId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Panier supprimé avec succès !");

                    // Mettre à jour l'affichage
                    mettreAJourAffichage();

                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de supprimer le panier.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du panier : " + e.getMessage());
        }
    }

    private void mettreAJourAffichage() {

        removeAll();

        // Recharger le contenu

        // Panneau pour le bouton Retour
        InterfacePrincipale interPr = new InterfacePrincipale();
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(e -> {

            interPr.onClientLoggedIn(clientId);

            // Ferme la fenêtre courant
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
        });
        topPanel.add(retourButton, BorderLayout.WEST);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("Informations du Client #" + clientId);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        // Ajout des labels d'information client
        nomLabel = createInfoLabel("Nom", gbc, 1, contentPanel);
        prenomLabel = createInfoLabel("Prénom", gbc, 3, contentPanel);
        emailLabel = createInfoLabel("Email", gbc, 5, contentPanel);
        telephoneLabel = createInfoLabel("Téléphone", gbc, 7, contentPanel);
        adresseLabel = createInfoLabel("Adresse", gbc, 9, contentPanel);

        // Affichage du panier en cours
        afficherCommandeEnCours(gbc, contentPanel);

        // Boutons de gestion
        JPanel buttonPanel = new JPanel(new FlowLayout());
        validerPanierButton = new JButton("Valider votre commande");
        supprimerPanierButton = new JButton("Annuler la commande");
        buttonPanel.add(validerPanierButton);
        buttonPanel.add(supprimerPanierButton);

        // Ajouter le panneau des boutons
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbc);

        // Ajouter les listeners pour les boutons
        validerPanierButton.addActionListener(e -> {
            try {
                validerCommande();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        supprimerPanierButton.addActionListener(e -> {
            try {
                supprimerCommande();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        chargerInformationsClient();

        revalidate();
        repaint();
    }

}
