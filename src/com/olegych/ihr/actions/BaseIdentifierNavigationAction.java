package com.olegych.ihr.actions;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.highlighting.HighlightManagerImpl;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * @author OlegYch
 */
class BaseIdentifierNavigationAction extends AnAction {
  protected final String navigationActionId;

  public BaseIdentifierNavigationAction(String navigationActionId) {
    this.navigationActionId = navigationActionId;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    navigate(e, navigationActionId);
  }

  private void navigate(AnActionEvent e, String navigationActionId) {
    final Editor editor = PlatformDataKeys.EDITOR.getData(e.getDataContext());
    final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    if (editor != null && project != null) {
      final boolean alwaysClearHighlights = getAlwaysClear();
      final boolean clearHighlights = HighlightUsagesHandler.isClearHighlights(editor);
      if (alwaysClearHighlights) {
        ((HighlightManagerImpl) HighlightManager.getInstance(project))
            .hideHighlights(editor, HighlightManager.HIDE_BY_ESCAPE | HighlightManager.HIDE_BY_ANY_KEY);
      } else if (clearHighlights) {
        e.getActionManager().getAction("HighlightUsagesInFile").actionPerformed(e);
      }
      e.getActionManager().getAction("HighlightUsagesInFile").actionPerformed(e);
      e.getActionManager().getAction(navigationActionId).actionPerformed(e);
      editor.getSelectionModel().removeSelection();
    }
  }

  private boolean getAlwaysClear() {
    return true;
  }
}
