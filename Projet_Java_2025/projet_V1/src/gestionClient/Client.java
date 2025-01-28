package gestionClient;

public class Client {

    private int idClient;
    private String nomClient;
    private String prenomClient;
    private String emailClient;
    private String adresseClient;
    private int telephoneClient;
    private String loginClient;
    private String motdepasseClient;

   // private static int COMPTEUR_CLIENT = 0;

    public Client(int id, String nom, String prenom, String email, String adresse, int telephone, String login, String motdepasse) {
        this.idClient = id;
        this.nomClient = nom;
        this.prenomClient = prenom;
        this.emailClient = email;
        this.adresseClient = adresse;
        this.telephoneClient = telephone;
        this.loginClient = login;
        this.motdepasseClient = motdepasse;
    }

    public Client(String nom, String prenom, String email, String adresse, int telephone, String login, String motdepasse) {
        this.nomClient = nom;
        this.prenomClient = prenom;
        this.emailClient = email;
        this.adresseClient = adresse;
        this.telephoneClient = telephone;
        this.loginClient = login;
        this.motdepasseClient = motdepasse;
    }

    public Client(String login, String motdepasse){
        this.loginClient = login;
        this.motdepasseClient = motdepasse;
    }

    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }
    public void setNomClient(String nom) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }
    public void setPrenomClient(String prenom) {
        this.prenomClient = prenom;
    }

    public String getEmailClient() {
        return emailClient;
    }
    public void setEmailClient(String email) {
        this.emailClient = email;
    }

    public String getAdresseClient() {
        return adresseClient;
    }
    public void setAdresseClient(String adresse) {
        this.adresseClient = adresse;
    }

    public int getTelephoneClient() {
        return telephoneClient;
    }
    public void setTelephoneClient(int telephone) {
        this.telephoneClient = telephone;
    }

    public String getLoginClient() {
        return loginClient;
    }
    public void setLoginClient(String login) {
        this.loginClient = login;
    }

    public String getMotdepasseClient() {
        return motdepasseClient;
    }
    public void setMotdepasseClient(String motdepasse) {
        this.motdepasseClient = motdepasse;
    }

    @Override
    public String toString() {
        return
                ", prénom='" + getPrenomClient() + " #" + getIdClient() + '\t' +
                ", nom='" + getNomClient() + '\t' +
                ", email='" + getEmailClient() + '\'' +
                ", adresse='" + getAdresseClient() + '\'' +
                ", telephone='" + getTelephoneClient() + '\'' +
                ", login='" + getLoginClient() + '\''
        ;
    }

}
