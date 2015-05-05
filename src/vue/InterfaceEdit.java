package vue;

import controleur.CreateParser;
import controleur.Utilities;
import modele.Chunk;
import modele.Corpus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.DimensionUIResource;
import java.util.List;
/**
 * Created by SIN on 27/04/2015.
 *
 * travail � faire : initialiser les liste , cr�er une liste de mots ,
 * creer un parseur invers� (class creatparser)
 * ajout� les m�thode set au chunk voir cr�er un nouveau mod�le pour Aedit m�thode set principalement
 * le createparser doit remplir le mod�le Aedit
 */

public class InterfaceEdit extends JFrame implements ActionListener, MouseListener{
    private JPanel interfaceEdit;
    private JPanel panelChunks;
    private JPanel panelLiaison;
    private JPanel panelMots;
    private JButton boutonRetour;
    private JList liste;
    private JList listeLiaison;
    private JDialog fenetreDeFin;
    private JLabel mot;
    private List < String > listeMot;
    private Chunk chunkCourant;
    private String motCourant;

    private int compteurChunk;
    private int compteurMot;

    private List<Chunk> listeChunks;
    private DefaultListModel modeleDeListe;
    private DefaultListModel modeleDeListeLiaison;

    private int compteurLiaison = 1;
    private int compteurUtilisationLiaison = 0;

    /*Initialisation de la fen�tre */
    public InterfaceEdit(String fichierXML){
        Utilities.initFenetre(this, interfaceEdit);
        initListe();
        initPanelMot(fichierXML);
        ajouterListener();

    }

    private void initListe()
    {
        modeleDeListe = new DefaultListModel();
        modeleDeListe.ensureCapacity(100);
        modeleDeListe.addElement("");
        modeleDeListeLiaison = new DefaultListModel();
        modeleDeListeLiaison.ensureCapacity(100);
        for (int i = 0; i < 1000; i++)
            modeleDeListeLiaison.addElement(" ");

        liste = new JList();
        liste.setModel(modeleDeListe);
        listeLiaison = new JList();
        listeLiaison.setModel(modeleDeListeLiaison);

        JScrollPane scrollPane = new JScrollPane(liste);
        JScrollPane scrollPaneLiaison = new JScrollPane(listeLiaison);
        scrollPane.setBackground(new Color(73, 200, 232));
        scrollPane.getVerticalScrollBar().setModel(scrollPaneLiaison.getVerticalScrollBar().getModel());

        liste.addMouseListener(this);
        panelChunks.add(scrollPane);
        panelLiaison.add(scrollPaneLiaison);
    }

    private void ajouterListener()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener
                (
                        new AWTEventListener() {
                            public void eventDispatched(AWTEvent event) {
                                KeyEvent ke = (KeyEvent) event;
                                if (ke.getID() == KeyEvent.KEY_RELEASED) {
                                } else if (ke.getID() == KeyEvent.KEY_PRESSED) {

                                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                                        deplacerMot();
                                    } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                                        deplacerChunk();

                                    }

                                }
                            }

                        } , AWTEvent.KEY_EVENT_MASK);

        boutonRetour.addActionListener(this);
    }

    private void deplacerMot() {
        String motCourant = panelMots.getComponent(0).getName();
        modeleDeListe.setElementAt(((String) modeleDeListe.lastElement()).concat(" " + motCourant), modeleDeListe.getSize()-1);
        changerMot();

    }

    /**
     * Fonction qui change de mot apr�s avoir appuy� sur une des fleches
     */
    private void changerMot() {
        compteurMot++;

        if (chunkCourant.getListeMots().size() <= compteurMot)
        {
            compteurChunk++;

            if (listeChunks.size() <= compteurChunk)
            {
                compteurChunk = 0;
                compteurMot = 0;
                ecranDeFin();

                return;

            }

            chunkCourant = listeChunks.get(compteurChunk);
            compteurMot = 0;
        }

        motCourant = chunkCourant.getListeMots().get(compteurMot);
        definirLabelMot(motCourant);
    }

    private void deplacerChunk() {
        String motCourant = panelMots.getComponent(0).getName();
        modeleDeListe.addElement(motCourant);
        changerMot();
    }

    private void initPanelMot(String fichierXML){
        createparser(fichierXML);
        JTextArea texteExp = new JTextArea("Fleche gauche : rajouter au chunk \n Fleche du bas : ajouter � un nouveau chunk");
        texteExp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        texteExp.setBackground(new Color(73, 200, 232));
        texteExp.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMots.add(texteExp, BorderLayout.CENTER);


    }

    private void createparser(String fichierXML){
        CreateParser createparseur = new CreateParser(fichierXML);

        listeMot = createparseur.afficheAll();

            chunkCourant = listeChunks.get(compteurChunk);
            motCourant = chunkCourant.getListeMots().get(compteurMot);
            ajouterPremierMot(motCourant);
    }

    public void ajouterPremierMot(String texte){
        mot = new JLabel();
        definirLabelMot(texte);
        mot.setHorizontalAlignment(JLabel.CENTER);
        mot.setVerticalAlignment(JLabel.CENTER);
        mot.setAlignmentX(Component.CENTER_ALIGNMENT);
        mot.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panelMots.add(mot, BorderLayout.SOUTH);
    }

    public void definirLabelMot(String nom){
        mot.setName(nom);
        mot.setText(nom);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        /*mise en place de l'interface pour les remplirs de mani�re dynamique*/
        panelLiaison = new JPanel();
        panelLiaison.setLayout(new BorderLayout());
        panelLiaison.setBackground(new Color(93, 135, 133));

        panelChunks = new JPanel();
        panelChunks.setLayout(new BorderLayout());
        panelChunks.setBackground(new Color(60, 63, 65));

    }


    /**
     * M�thode qui initialise la fen�tre de fin
     */
    private void ecranDeFin() {
        fenetreDeFin = new JDialog(this);
        JPanel panelDeFin = new JPanel(new BorderLayout());
        JTextArea texteDeFin = new JTextArea();
        texteDeFin.setText("Bravo ! Vous avez fini ce niveau. Cliquez sur OK pour retournez � l'�cran de choix de niveau.");
        JButton boutonOk = new JButton("OK");
        boutonOk.setVerticalAlignment(SwingConstants.CENTER);
        boutonOk.setHorizontalAlignment(SwingConstants.CENTER);
        boutonOk.addActionListener(this);
        panelDeFin.add(texteDeFin, BorderLayout.NORTH);
        panelDeFin.add(boutonOk, BorderLayout.SOUTH);
        fenetreDeFin.add(panelDeFin);
        fenetreDeFin.pack();
        fenetreDeFin.setLocationRelativeTo(null);
        fenetreDeFin.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "OK")
        {
            fenetreDeFin.dispose();
            this.dispose();

            Edit edit = new Edit();
        }
        else if (e.getActionCommand() == "Retour")
        {
            this.dispose();
            Edit edit = new Edit();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 1) {
            int index = liste.locationToIndex(e.getPoint());
            modeleDeListeLiaison.setElementAt(compteurLiaison, index);
            compteurUtilisationLiaison++;
            if (compteurUtilisationLiaison == 2) {
                compteurUtilisationLiaison = 0;
                compteurLiaison++;
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

