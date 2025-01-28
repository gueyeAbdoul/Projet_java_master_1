package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceGestionPanier extends JPanel {
    private int clientId; // ID du client connecté
    private int panierEnCoursId; // ID du panier en cours
    private JPanel panierPanel; // Panel pour afficher les produits d'une panier
    private JButton validerPanierButton, supprimerPanierButton;

    public int getPanierEnCoursId() throws SQLException {

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String selectQuery = """
                    SELECT idPanier FROM panier WHERE idClient = ? AND etatPanier = 'en cours'
                    """
            ;
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                panierEnCoursId = rs.getInt("idPanier");
            } else {
                panierEnCoursId = 0;
            }
        }
        return panierEnCoursId;
    }

    public InterfaceGestionPanier(int clientId) {
        this.clientId = clientId;
        this.panierEnCoursId = 0; // Pas de panier active au début

        setLayout(new BorderLayout());
        JLabel header = new JLabel("Gestion du panier du client #" + this.clientId, SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        // Panneau pour les paniers
        panierPanel = new JPanel();
        panierPanel.setLayout(new BoxLayout(panierPanel, BoxLayout.Y_AXIS)); // BoxLayout pour affichage vertical
        JScrollPane scrollPane = new JScrollPane(panierPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajout de marges
        add(scrollPane, BorderLayout.CENTER);

        // Boutons de gestion
        JPanel buttonPanel = new JPanel(new FlowLayout());
        validerPanierButton = new JButton("Passer votre commande");
        supprimerPanierButton = new JButton("vider le panier");
        buttonPanel.add(validerPanierButton);
        buttonPanel.add(supprimerPanierButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Appel pour afficher les paniers du client
        affichePanierClient();

        // Ajout des actions
        validerPanierButton.addActionListener(e -> {
            try {
                validerPanier();

                // Crée une nouvelle instance de InterfaceGestionCommande
                InterfaceGestionCommande gestionCommande = new InterfaceGestionCommande(clientId);

                // Créer un nouveau JFrame pour afficher la fenêtre de gestion des commandes
                JFrame commandeFrame = new JFrame("Gestion des Commandes");
                commandeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                commandeFrame.setContentPane(gestionCommande);
                commandeFrame.setSize(800, 600);  // Ajustez la taille
                commandeFrame.setLocationRelativeTo(null);  // Centrer la fenêtre
                commandeFrame.setVisible(true);

                // Fermer la fenêtre actuelle (la fenêtre de gestion du panier)
                SwingUtilities.getWindowAncestor(validerPanierButton).dispose();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        supprimerPanierButton.addActionListener(e -> {
            try {
                supprimerPanier();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void refresh() {
        affichePanierClient(); // Appel la méthode pour mettre à jour l'affichage du panier
    }

//    public void affichePanierClient() {
//        panierPanel.removeAll(); // Réinitialiser l'affichage
//        panierPanel.setLayout(new BorderLayout()); // Organise les éléments
//
//        JPanel produitsPanel = new JPanel(new GridLayout(0, 1, 2, 2)); // Produits en grille
//
//        JPanel infoPanierPanel = new JPanel();
//
//        boolean panierVide = true; // Détecte si le panier est vide
//
//        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
//            String sql = """
//        SELECT pa.idpanier, p.idProduit, p.nomProduit, p.prixProduit, p.imageProduit
//        FROM produit p
//        INNER JOIN produit_panier pc ON pc.idProduit = p.idProduit
//        INNER JOIN panier pa ON pa.idpanier = pc.idpanier
//        WHERE pa.etatpanier = 'en cours' AND pa.idclient = ?
//        """;
//
//            PreparedStatement statement = conn.prepareStatement(sql);
//            statement.setInt(1, clientId);
//            ResultSet rs = statement.executeQuery();
//
//            int idPanierCourant = -1; // Pour détecter le changement de panier
//
//            while (rs.next()) {
//                panierVide = false; // Le panier contient des produits
//                int idPanier = rs.getInt("idpanier");
//                int idProduit = rs.getInt("idProduit");
//
//                String imageProduit = rs.getString("imageProduit");
//
//                // Détecter le panier actif
//                if (idPanier != idPanierCourant) {
//                    idPanierCourant = idPanier;
//
//                    // Met à jour les informations du panier actuel
//                    if (infoPanierPanel.getComponentCount() > 0) {
//                        infoPanierPanel.removeAll(); // Supprimer l'ancien panneau si existant
//                    }
//                    JPanel panneauPanier = informationPanier(idPanierCourant);
//                    infoPanierPanel.add(panneauPanier);
//                }
//
//                // Afficher chaque produit
//                JPanel produitPanel = new JPanel(new BorderLayout());
//                produitPanel.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 100));
//
//                // Image du produit
//                JLabel produitImage = new JLabel();
//                produitImage.setIcon(new ImageIcon(imageProduit));
//                produitPanel.add(produitImage, BorderLayout.WEST);
//
//                // Boutons
//                JPanel buttonPanel = new JPanel();
//                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//                buttonPanel.add(Box.createVerticalStrut(75)); // Espacement
//                JButton detailsButton = new JButton("Détails");
//                detailsButton.addActionListener(e -> afficherDetailsProduit(idProduit));
//                JButton supprimerButton = new JButton("Supprimer");
//                supprimerButton.addActionListener(e -> supprimerProduitDePanier(idProduit));
//                buttonPanel.add(detailsButton);
//                buttonPanel.add(Box.createVerticalStrut(25)); // Espacement
//                buttonPanel.add(supprimerButton);
//
//                produitPanel.add(buttonPanel, BorderLayout.EAST);
//
//                // Ajouter le produit au panneau des produits
//                produitsPanel.add(produitPanel);
//            }
//
//            // Si aucun produit n'est détecté, afficher l'image et la phrase
//            if (panierVide) {
//                JPanel panneauVide = new JPanel(new BorderLayout());
//
//                // Redimensionner l'image
//                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/interfaceGraphique/gestionproduit/panier.png"));
//                Image image = imageIcon.getImage().getScaledInstance(700, 400, Image.SCALE_SMOOTH);
//                JLabel imageVide = new JLabel(new ImageIcon(image));
//                imageVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer l'image horizontalement
//
//                // Ajouter la phrase
//                JLabel texteVide = new JLabel("Votre panier est vide");
//                texteVide.setForeground(Color.RED);
//                texteVide.setFont(new Font("Arial", Font.BOLD, 30));
//                texteVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
//
//                panneauVide.add(imageVide, BorderLayout.CENTER);
//                panneauVide.add(texteVide, BorderLayout.SOUTH);
//
//                panierPanel.add(panneauVide, BorderLayout.CENTER);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage du panier.", "Erreur", JOptionPane.ERROR_MESSAGE);
//        }
//
//        if (!panierVide) {
//            // Ajouter les panneaux au panneau principal
//            panierPanel.add(new JScrollPane(produitsPanel), BorderLayout.CENTER);
//            panierPanel.add(infoPanierPanel, BorderLayout.SOUTH);
//        }
//
//        panierPanel.revalidate();
//        panierPanel.repaint();
//    }
public void affichePanierClient() {
    panierPanel.removeAll(); // Réinitialiser l'affichage
    panierPanel.setLayout(new BorderLayout()); // Organise les éléments

    JPanel produitsPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 colonnes, espacement entre produits

    JPanel infoPanierPanel = new JPanel();

    boolean panierVide = true; // Détecte si le panier est vide

    try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
        String sql = """
            SELECT pa.idpanier, p.idProduit, p.nomProduit, p.prixProduit, p.imageProduit
            FROM produit p
            INNER JOIN produit_panier pc ON pc.idProduit = p.idProduit
            INNER JOIN panier pa ON pa.idpanier = pc.idpanier
            WHERE pa.etatpanier = 'en cours' AND pa.idclient = ?
            """;

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, clientId);
        ResultSet rs = statement.executeQuery();

        int idPanierCourant = -1; // Pour détecter le changement de panier

        while (rs.next()) {
            panierVide = false; // Le panier contient des produits
            int idPanier = rs.getInt("idpanier");
            int idProduit = rs.getInt("idProduit");

            String imageProduit = rs.getString("imageProduit");

            // Détecter le panier actif
            if (idPanier != idPanierCourant) {
                idPanierCourant = idPanier;

                // Met à jour les informations du panier actuel
                if (infoPanierPanel.getComponentCount() > 0) {
                    infoPanierPanel.removeAll(); // Supprimer l'ancien panneau si existant
                }
                JPanel panneauPanier = informationPanier(idPanierCourant);
                infoPanierPanel.add(panneauPanier);
            }

            // Afficher chaque produit
            JPanel produitPanel = new JPanel(new BorderLayout());
            produitPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Image du produit
            JLabel produitImage = new JLabel();
            produitImage.setHorizontalAlignment(SwingConstants.CENTER);

            // Charger et redimensionner l'image
            try {
                ImageIcon icon = new ImageIcon(imageProduit);
                Image scaledImage = icon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH); // Ajuster la taille
                produitImage.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                produitImage.setText("Image non disponible"); // Gestion si l'image est introuvable
            }

            produitPanel.add(produitImage, BorderLayout.CENTER);

            // Boutons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.add(Box.createVerticalStrut(10)); // Espacement
            JButton detailsButton = new JButton("Détails");
            detailsButton.addActionListener(e -> afficherDetailsProduit(idProduit));
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.addActionListener(e -> supprimerProduitDePanier(idProduit));
            buttonPanel.add(detailsButton);
            buttonPanel.add(Box.createVerticalStrut(10)); // Espacement
            buttonPanel.add(supprimerButton);

            produitPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Ajouter le produit au panneau des produits
            produitsPanel.add(produitPanel);
        }

        // Si aucun produit n'est détecté, afficher l'image et la phrase
        if (panierVide) {
            JPanel panneauVide = new JPanel(new BorderLayout());

            // Redimensionner l'image
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/interfaceGraphique/gestionproduit/panier.png"));
            Image image = imageIcon.getImage().getScaledInstance(700, 400, Image.SCALE_SMOOTH);
            JLabel imageVide = new JLabel(new ImageIcon(image));
            imageVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer l'image horizontalement

            // Ajouter la phrase
            JLabel texteVide = new JLabel("Votre panier est vide");
            texteVide.setForeground(Color.RED);
            texteVide.setFont(new Font("Arial", Font.BOLD, 30));
            texteVide.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte

            panneauVide.add(imageVide, BorderLayout.CENTER);
            panneauVide.add(texteVide, BorderLayout.SOUTH);

            panierPanel.add(panneauVide, BorderLayout.CENTER);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage du panier.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    if (!panierVide) {
        // Ajouter les panneaux au panneau principal
        panierPanel.add(new JScrollPane(produitsPanel), BorderLayout.CENTER);
        panierPanel.add(infoPanierPanel, BorderLayout.SOUTH);
    }

    panierPanel.revalidate();
    panierPanel.repaint();
}


    private JPanel informationPanier(int idPanier) {
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            // Calculer le montant total du panier
            String sqlMontant = """
            SELECT SUM(p.prixProduit * pp.quantiteproduit) AS montantTotal
            FROM produit p
            INNER JOIN produit_panier pp ON pp.idProduit = p.idProduit
            WHERE pp.idPanier = ?
            """;
            PreparedStatement statementMontant = conn.prepareStatement(sqlMontant);
            statementMontant.setInt(1, idPanier);
            ResultSet rsMontant = statementMontant.executeQuery();

            float montantTotal = 0;
            if (rsMontant.next()) {
                montantTotal = rsMontant.getFloat("montantTotal");
            }

            // Mettre à jour le montant total dans la table panier
            String sqlUpdate = "UPDATE panier SET montantpanier = ? WHERE idPanier = ?";
            PreparedStatement statementUpdate = conn.prepareStatement(sqlUpdate);
            statementUpdate.setFloat(1, montantTotal);
            statementUpdate.setInt(2, idPanier);
            statementUpdate.executeUpdate();

            // Récupérer les informations  du panier
            String sqlPanier = """
            SELECT etatPanier, datePanier, montantPanier
            FROM panier
            WHERE idPanier = ?
            """;
            PreparedStatement statementPanier = conn.prepareStatement(sqlPanier);
            statementPanier.setInt(1, idPanier);
            ResultSet rsPanier = statementPanier.executeQuery();

            if (rsPanier.next()) {
                String etatPanier = rsPanier.getString("etatPanier");
                Date datePanier = rsPanier.getDate("datePanier");
                float montantPanier = rsPanier.getFloat("montantPanier");

                // Ajouter les informations au panneau
                JLabel etatLabel = new JLabel("État : " + etatPanier, SwingConstants.CENTER);
                JLabel dateLabel = new JLabel("Date : " + datePanier, SwingConstants.CENTER);
                JLabel montantLabel = new JLabel("Montant : " + montantPanier + "€", SwingConstants.CENTER);

                infoPanel.add(etatLabel);
                infoPanel.add(dateLabel);
                infoPanel.add(montantLabel);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des informations du panier", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return infoPanel;
    }

    // méthode pour supprimer un produit dans le panier
    private void supprimerProduitDePanier(int idProduit) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce produit dans le panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
                String sql = "DELETE FROM produit_panier\n" +
                        "WHERE idProduit = ?\n" +
                        "  AND idPanier IN (\n" +
                        "    SELECT idPanier\n" +
                        "    FROM panier\n" +
                        "    WHERE etatPanier = 'en cours'\n" +
                        "  )";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, idProduit);
                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Produit supprimé avec succès.");
                    affichePanierClient(); // Rafraîchir l'affichage
                } else {
                    JOptionPane.showMessageDialog(this, "Le produit n'a pas pu être supprimé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void afficherDetailsProduit(int idProduit) {
        System.out.println("idProduit : " + idProduit);
        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String sql = "SELECT * FROM produit WHERE idProduit = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, idProduit);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String nomProduit = rs.getString("nomProduit");
                String categorieProduit = rs.getString("categorie");
                float prix = rs.getFloat("prixProduit");
                String description = rs.getString("descriptionProduit");

                // Afficher une boîte de dialogue avec les détails du produit
                JOptionPane.showMessageDialog(this,
                        "Nom du produit : " + nomProduit + "\nCategorie "+ categorieProduit + "\nPrix : " + prix + "€\nDescription : " + description,
                        "\nDétails du produit", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des détails du produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void validerPanier() throws SQLException {
        int idpa = getPanierEnCoursId();
        if (clientId == 0 || idpa == 0) {
            JOptionPane.showMessageDialog(null, "Aucun panier en cours à valider.");
            return;
        }

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            // Valider le panier
            String updatePanierQuery = "UPDATE Panier SET etatpanier = 'Validé' WHERE idpanier = ? AND idclient = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updatePanierQuery)) {
                stmt.setInt(1, idpa);
                stmt.setInt(2, clientId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Créer une nouvelle commande
                    if (creerCommande(conn, idpa)) {
                        JOptionPane.showMessageDialog(null, "Panier validé avec succès !");
                        idpa = 0; // Réinitialiser le panier en cours
                        affichePanierClient();
                    } else {
                        JOptionPane.showMessageDialog(null, "Le panier est vide");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de valider le panier");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la validation : " + e.getMessage());
        }
    }

    private boolean creerCommande(Connection conn, int idPanier) throws SQLException {
        String totalQuery = """
        SELECT SUM(produit.prixproduit) AS total
        FROM produit_panier
        INNER JOIN produit ON produit_panier.idProduit = produit.idProduit
        WHERE produit_panier.idPanier = ?
        """;
        double montantTotal = 0.0;

        try (PreparedStatement stmt = conn.prepareStatement(totalQuery)) {
            stmt.setInt(1, idPanier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                montantTotal = rs.getDouble("total");
            }
        }

        if (montantTotal <= 0) {
            return false; // Le panier est vide
        }

        // Insérer une nouvelle commande
        String insertCommandeQuery = """
        INSERT INTO commande (idClient, idpanier, etatCommande, montantCommande, datecommande)
        VALUES (?, ?, 'en cours', ?, NOW())
        """;
        try (PreparedStatement stmt = conn.prepareStatement(insertCommandeQuery)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, idPanier);
            stmt.setDouble(3, montantTotal);
            stmt.executeUpdate();
        }

        return true;
    }

    public void supprimerPanier() throws SQLException {
        int idpa = getPanierEnCoursId();
        if (clientId == 0 || idpa == 0) {
            System.out.println("idpa : " + idpa + " || idclient : " + clientId);
            JOptionPane.showMessageDialog(null, "Aucun panier en cours");
            return;
        }

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String query = "DELETE FROM Panier WHERE idpanier = ? AND idclient = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idpa);
                stmt.setInt(2, clientId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Panier supprimé avec succès !");
                   // idpa = 0; // Réinitialiser le panier en cours
                    affichePanierClient();
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de supprimer le panier.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du panier : " + e.getMessage());
        }
    }

    public void ajouterProduit(String nomProduit, float prix) throws SQLException {
        int idPan = getPanierEnCoursId();
        // Vérifiez si un client est connecté
        if (clientId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez vous identifier pour ajouter des produits",
                    "Accès refusé", JOptionPane.WARNING_MESSAGE);
            return; // Stoppe l'exécution
        }

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            // Vérifiez l'existence du client dans la base
            if (!verifierClientExistant(conn, clientId)) {
                JOptionPane.showMessageDialog(this, "Utilisateur introuvable. Veuillez vous reconnecter.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gestion du panier
            if (idPan == 0) {
                panierEnCoursId = creerPanierSiNecessaire(conn);
            }

            // Ajout du produit
            int produitId = obtenirProduitId(conn, nomProduit);
            if (produitId != -1) {
                ajouterProduitDansPanier(conn, panierEnCoursId, produitId, prix);
                mettreAJourMontantPanier(conn, panierEnCoursId);
                JOptionPane.showMessageDialog(this, "Produit ajouté : " + nomProduit);
            } else {
                JOptionPane.showMessageDialog(this, "Produit introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du produit au panier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour vérifier l'existence du client
    private boolean verifierClientExistant(Connection conn, int clientId) throws SQLException {
        String selectQuery = "SELECT idclient FROM client WHERE idClient = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private int creerPanierSiNecessaire(Connection conn) throws SQLException {
        String selectQuery = "SELECT idPanier FROM panier WHERE idClient = ? AND etatPanier = 'en cours'";
        try (PreparedStatement ps = conn.prepareStatement(selectQuery)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idPanier");
            }
        }

        // Si aucun panier active, créer une nouvelle
        String insertQuery = "INSERT INTO panier(idClient, etatpanier, montantpanier, datepanier) VALUES (?, 'en cours', 0, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clientId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("Impossible de créer un nouveau produit");
    }

    private int obtenirProduitId(Connection conn, String nomProduit) throws SQLException {
        String query = "SELECT idProduit FROM produit WHERE nomProduit = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomProduit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idProduit");
            }
        }
        return -1; // Produit non trouvé
    }

    private void ajouterProduitDansPanier(Connection conn, int panierId, int produitId, float prixProdPan) throws SQLException {
        // Vérifier si le produit existe déjà dans le panier
        String selectQuery = "SELECT quantiteproduit FROM produit_panier WHERE idPanier = ? AND idProduit = ?";
        try (PreparedStatement selectPs = conn.prepareStatement(selectQuery)) {
            selectPs.setInt(1, panierId);
            selectPs.setInt(2, produitId);

            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                // Si le produit existe, mettre à jour
                int quantiteExistante = rs.getInt("quantiteproduit");
                String updateQuery = "UPDATE produit_panier SET quantiteproduit = ?, prixproduitpanier = ? WHERE idPanier = ? AND idProduit = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                    updatePs.setInt(1, quantiteExistante + 1);
                    updatePs.setFloat(2, prixProdPan * (quantiteExistante + 1)); // Prix total
                    updatePs.setInt(3, panierId);
                    updatePs.setInt(4, produitId);
                    updatePs.executeUpdate();
                }
            } else {
                // Si le produit n'existe pas, insérer
                String insertQuery = "INSERT INTO produit_panier (idpanier, idProduit, quantiteproduit, prixproduitpanier) VALUES (?, ?, 1, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                    insertPs.setInt(1, panierId);
                    insertPs.setInt(2, produitId);
                    insertPs.setFloat(3, prixProdPan);
                    insertPs.executeUpdate();
                }
            }
        }
    }

    private void mettreAJourMontantPanier(Connection conn, int panierId) throws SQLException {
        String query = "UPDATE panier SET montantPanier = ("
                + "    SELECT SUM(quantiteproduit * prixproduitpanier) FROM produit_panier WHERE idPanier = ?"
                + ") WHERE idPanier = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, panierId);
            ps.setInt(2, panierId);
            ps.executeUpdate();
        }
    }

}
