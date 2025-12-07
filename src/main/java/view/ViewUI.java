
package view;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ViewUI {
    private static final JTextArea logArea = new JTextArea();
    private static final JFrame frame= new JFrame("PdfToJpeg - Traitement");
    private final AtomicBoolean userConfirmed = new AtomicBoolean(false);

    public ViewUI() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 400);

        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void appendLogStatic(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });

    }

    public boolean askUserForConfirmation(String msg) {
        int result = JOptionPane.showConfirmDialog(frame, msg, "Confirmation", JOptionPane.YES_NO_OPTION);
        userConfirmed.set(result == JOptionPane.YES_OPTION);
        return userConfirmed.get();
    }

    public void showBottomRightDialogAndExit() {
        // Crée le dialog
        JDialog dialog = new JDialog(frame, "Fin du traitement", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 120);

        // Ajoute le contenu
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Traitement terminé.\nOK pour quitter."), BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            dialog.dispose();
            frame.dispose();
            System.exit(0);
        });
        panel.add(okButton, BorderLayout.SOUTH);
        dialog.setContentPane(panel);

        // Calcule la position en bas à droite de la fenêtre principale
        Point parentLocation = frame.getLocationOnScreen();
        int x = parentLocation.x + frame.getWidth() - dialog.getWidth() - 20;
        int y = parentLocation.y + frame.getHeight() - dialog.getHeight() - 40;
        dialog.setLocation(x, y);

        dialog.setVisible(true);
    }
}
