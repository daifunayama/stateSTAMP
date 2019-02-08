package Utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop.Action;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextComponentPopupMenu extends JPopupMenu {
    protected TextComponentPopupMenu(JTextComponent tc) {
        super();

        UndoManager manager = new UndoManager();
        UndoAction undoAction = new UndoAction(manager);
        RedoAction redoAction = new RedoAction(manager);
        CutAction cutAction = new DefaultEditorKit.CutAction();
        CopyAction copyAction = new DefaultEditorKit.CopyAction();
        PasteAction pasteAction = new DefaultEditorKit.PasteAction();
        DeleteAction deleteAction = new DeleteAction();
//         Action deleteAction = new AbstractAction("delete") {
//             @Override public void actionPerformed(ActionEvent e) {
//                 ((JTextComponent) getInvoker()).replaceSelection(null);
//             }
//         };
        tc.addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) {
                manager.discardAllEdits();
                e.getComponent().requestFocusInWindow();
            }
            @Override public void ancestorMoved(AncestorEvent e) { /* not needed */ }
            @Override public void ancestorRemoved(AncestorEvent e) { /* not needed */ }
        });
        tc.getDocument().addUndoableEditListener(manager);
        tc.getActionMap().put("undo", undoAction);
        tc.getActionMap().put("redo", redoAction);
        InputMap imap = tc.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "undo");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "redo");

        add(cutAction);
        add(copyAction);
        add(pasteAction);
        add(deleteAction);
        addSeparator();
        add(undoAction);
        add(redoAction);

        addPopupMenuListener(new PopupMenuListener() {
            @Override public void popupMenuCanceled(PopupMenuEvent e) { /* not needed */ }
            @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                undoAction.setEnabled(true);
                redoAction.setEnabled(true);
            }
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JTextComponent tc = (JTextComponent) getInvoker();
                boolean hasSelectedText = Objects.nonNull(tc.getSelectedText());
                cutAction.setEnabled(hasSelectedText);
                copyAction.setEnabled(hasSelectedText);
                deleteAction.setEnabled(hasSelectedText);
                undoAction.setEnabled(manager.canUndo());
                redoAction.setEnabled(manager.canRedo());
            }
        });
    }
}

class UndoAction extends AbstractAction {
    private final UndoManager undoManager;
    protected UndoAction(UndoManager manager) {
        super("undo");
        this.undoManager = manager;
    }
    @Override public void actionPerformed(ActionEvent e) {
        try {
            undoManager.undo();
        } catch (CannotUndoException ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

class RedoAction extends AbstractAction {
    private final UndoManager undoManager;
    protected RedoAction(UndoManager manager) {
        super("redo");
        this.undoManager = manager;
    }
    @Override public void actionPerformed(ActionEvent e) {
        try {
            undoManager.redo();
        } catch (CannotRedoException ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

class DeleteAction extends AbstractAction {
    protected DeleteAction() {
        super("delete");
    }
    @Override public void actionPerformed(ActionEvent e) {
        // Container c = SwingUtilities.getAncestorOfClass(JPopupMenu.class, (Component) e.getSource());
        Container c = SwingUtilities.getUnwrappedParent((Component) e.getSource());
        if (c instanceof JPopupMenu) {
            JPopupMenu pop = (JPopupMenu) c;
            ((JTextComponent) pop.getInvoker()).replaceSelection(null);
        }
    }
}