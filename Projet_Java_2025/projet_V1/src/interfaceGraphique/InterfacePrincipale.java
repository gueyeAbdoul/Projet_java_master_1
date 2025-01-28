package interfaceGraphique;

import interfaceGraphique.gestionclient.ConnexionPanel;
import interfaceGraphique.gestionclient.InterfaceGestionClient;
import interfaceGraphique.gestionclient.InscriptionPanel;
import interfaceGraphique.gestionproduit.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterfacePrincipale extends JFrame {

    private JPanel cardPanel; // Panneau principal avec CardLayout
    private CardLayout cardLayout; // Gestionnaire de disposition
    private JButton actionButton; // Bouton qui change dynamiquement (S'inscrire / Connexion)

    public InterfacePrincipale() {
        setTitle("Application de Gestion de vente de ABDOUL GUEYE");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Utilisation de BackgroundPanel avec l'image de fond
        BackgroundPanel mainPanel = new BackgroundPanel("/interfaceGraphique/images1.jpg");
        mainPanel.setLayout(new GridBagLayout());

        // Phrase de bienvenue
        JLabel welcomeLabel = new JLabel("Bienvenue à notre application de gestion de vente d'ordinateurs portables et de smartphones.");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.RED);
        welcomeLabel.setForeground(new Color(0, 128, 0));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(welcomeLabel, gbc);

        // Initialisation du CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(new Dimension(700, 500));

        // Création des panneaux pour Connexion et Inscription
        ConnexionPanel connexionPanel = new ConnexionPanel(clientId -> onClientLoggedIn(clientId));
        InscriptionPanel inscriptionPanel = new InscriptionPanel(clientId -> onClientLoggedIn(clientId));

        // Ajout des panneaux au CardLayout
        cardPanel.add(connexionPanel, "Connexion");
        cardPanel.add(inscriptionPanel, "Inscription");

        // Ajouter le panneau CardLayout au centre du mainPanel
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(cardPanel, gbc);

        // Création du bouton d'action (S'inscrire / Connexion)
        actionButton = new JButton("S'inscrire");
        actionButton.addActionListener(e -> basculerFormulaire());

        // Ajouter le bouton d'action sous le formulaire
        gbc.gridy = 2;
        mainPanel.add(actionButton, gbc);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }

    private void basculerFormulaire() {
        // Vérifier le panneau actuellement affiché
        if (actionButton.getText().equals("S'inscrire")) {
            cardLayout.show(cardPanel, "Inscription");
            actionButton.setText("Connexion");
        } else {
            cardLayout.show(cardPanel, "Connexion");
            actionButton.setText("S'inscrire");
        }
    }

    public void onClientLoggedIn(int clientId) {
        dispose();

        JFrame clientInterface = new JFrame("Interface Client");
        clientInterface.setSize(800, 600);
        clientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientInterface.setLocationRelativeTo(null);

        CardLayout functionalityLayout = new CardLayout();
        JPanel functionalityPanel = new JPanel(functionalityLayout);

        // Ajout des fonctionnalités
        InterfaceGestionPanier gestionPanier = new InterfaceGestionPanier(clientId);
        InterfaceGestionProduit gestionProduit = new InterfaceGestionProduit(clientId, gestionPanier);
        InterfaceGestionClient gestionClient = new InterfaceGestionClient(clientId);
        InterfaceListeFacture listeFacture = new InterfaceListeFacture(clientId);
        InterfaceListeCommande listeCommande = new InterfaceListeCommande(clientId);

        functionalityPanel.add(gestionProduit, "Produits");
        functionalityPanel.add(gestionPanier, "Mon panier");
        functionalityPanel.add(gestionClient, "Compte");
        functionalityPanel.add(listeCommande, "Historiques des commandes");
        functionalityPanel.add(listeFacture, "Mes Facture");

        // Menu de navigation
        JPanel menuPanel = new JPanel();
        JButton produitsButton = new JButton("Produits");
        produitsButton.addActionListener(e -> functionalityLayout.show(functionalityPanel, "Produits"));
        JButton panierButton = new JButton("Mon panier");
        panierButton.addActionListener(e -> {
            functionalityLayout.show(functionalityPanel, "Mon panier");
            gestionPanier.refresh();
        });

        JButton voirInfoButton = new JButton("Compte");
        voirInfoButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        voirInfoButton.addActionListener(e -> functionalityLayout.show(functionalityPanel, "Compte"));
        JButton listeCommandeButton = new JButton("Historiques des commandes");
        listeCommandeButton.addActionListener(e -> {
            functionalityLayout.show(functionalityPanel, "Historiques des commandes");
        });
        JButton factureButton = new JButton("Mes Facture");
        factureButton.addActionListener(e -> {
            functionalityLayout.show(functionalityPanel, "Mes Facture");
        });
        menuPanel.add(produitsButton);
        menuPanel.add(panierButton);
        menuPanel.add(listeCommandeButton);
        menuPanel.add(factureButton);
        menuPanel.add(voirInfoButton);

        clientInterface.setLayout(new BorderLayout());
        clientInterface.add(menuPanel, BorderLayout.NORTH);
        clientInterface.add(functionalityPanel, BorderLayout.CENTER);

        clientInterface.setVisible(true);
    }

}