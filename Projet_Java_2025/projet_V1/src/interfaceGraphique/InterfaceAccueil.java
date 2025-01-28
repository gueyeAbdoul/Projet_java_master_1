package interfaceGraphique;

import interfaceGraphique.gestionproduit.InterfaceGestionPanier;
import interfaceGraphique.gestionproduit.InterfaceGestionProduit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class InterfaceAccueil extends JFrame {
    private int clientId; // ID du client connecté
    private InterfaceGestionPanier gestionPanierPanel;

    public InterfaceAccueil(int clientId, InterfaceGestionPanier gestionCommandePanel) {
        this.clientId = clientId;
        this.gestionPanierPanel = gestionCommandePanel;

        setTitle("Accueil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Configuration principale avec une bordure carrée
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 35),
                new EmptyBorder(5, 5, 5, 5)
        ));
        setContentPane(mainPanel);

        // Créer la barre supérieure avec le bouton "Voir Panier"
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBackground(Color.LIGHT_GRAY);

        JLabel header = new JLabel("Trouvez votre compagnon technologique idéal dans votre Application de vente préférée.", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.BLUE);
        topBar.add(header, BorderLayout.CENTER);

        JButton panierButton = new JButton("Se connecter");

        try {
            ImageIcon rawIcon = new ImageIcon(getClass().getResource("/interfaceGraphique/login.jpeg"));
            Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon panierIcon = new ImageIcon(scaledImage);
            panierButton.setIcon(panierIcon);
        } catch (Exception e) {
            System.err.println("Icône non trouvée : " + e.getMessage());
        }

        panierButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        panierButton.setIconTextGap(10);
        panierButton.setMargin(new Insets(5, 0, 5, 0)); // Padding
        panierButton.setPreferredSize(new Dimension(200, 70)); // Taille minimale

        panierButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                InterfacePrincipale principale = new InterfacePrincipale();
                principale.setVisible(true);
            });
            dispose();
        });

        topBar.add(panierButton, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Afficher les produits avec InterfaceGestionProduit
        InterfaceGestionProduit gestionProduitPanel = new InterfaceGestionProduit(clientId, gestionCommandePanel);
        mainPanel.add(gestionProduitPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        int clientId = -1;
        InterfaceGestionPanier gestionCommandePanel = new InterfaceGestionPanier(clientId);
        new InterfaceAccueil(clientId, gestionCommandePanel);
    }
}
