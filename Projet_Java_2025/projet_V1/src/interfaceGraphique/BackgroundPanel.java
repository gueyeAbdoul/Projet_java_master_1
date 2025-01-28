package interfaceGraphique;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // Charger l'image comme ressource
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                throw new IllegalArgumentException("Ressource introuvable : " + imagePath);
            }
            backgroundImage = new ImageIcon(resource).getImage();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de fond : " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            // Obtenez les dimensions actuelles du panneau
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Redimensionnez l'image à une taille spécifique ou selon le panneau
            int imageWidth = 1400;   // Adaptez ici pour une largeur spécifique
            int imageHeight = 800; // Adaptez ici pour une hauteur spécifique

            // Dessinez l'image redimensionnée
            g.drawImage(backgroundImage, 0, 0, imageWidth, imageHeight, this);
        }
    }
}
