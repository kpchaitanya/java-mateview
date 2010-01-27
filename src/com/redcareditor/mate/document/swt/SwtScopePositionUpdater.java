package com.redcareditor.mate.document.swt;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;

/**
 * ScopePositionUpdater 
 * 
 * A position updater that never deletes a position. If the region containing
 * the position is deleted, the position is moved to the beginning/end (falling
 * together) of the change. If the region containing the position is replaced,
 * the position is placed at the same location inside the replacement text, but
 * always inside the replacement text.
 *
 * @since 3.1
 */
public class SwtScopePositionUpdater implements IPositionUpdater {
	/** The position category. */
	private final String fCategory;

	/**
	 * Creates a new updater for the given <code>category</code>.
	 *
	 * @param category the new category.
	 */
	public SwtScopePositionUpdater(String category) {
		fCategory= category;
	}

	/*
	 * @see org.eclipse.jface.text.IPositionUpdater#update(org.eclipse.jface.text.DocumentEvent)
	 */
	public void update(DocumentEvent event) {
		int eventOffset       = event.getOffset();
		int eventOldEndOffset = eventOffset + event.getLength();
		int eventNewLength    = event.getText() == null ? 0 : event.getText().length();
		int eventNewEndOffset = eventOffset + eventNewLength;
		int deltaLength       = eventNewLength - event.getLength();
		// System.out.printf("SwtScopePositionUpdater delta:%d\n", deltaLength);

		try {
			Position[] positions= event.getDocument().getPositions(fCategory);
			for (int i= 0; i != positions.length; i++) {

				Position position= positions[i];

				if (position.isDeleted())
					continue;

				int posOffset = position.getOffset();
				int posLength = position.getLength();  // always zero
				int posEnd    = posOffset + posLength;
				// System.out.printf("  position %d offset:%d length:%d end:%d\n", i, posOffset, posLength, posEnd);
				
				if (posOffset > eventOldEndOffset) {
					// position comes way after change - shift
					position.setOffset(posOffset + deltaLength);
				} else if (posOffset < eventOffset) {
					// position comes way before change - leave alone
				} else {
					// position is within replaced text - move it to the end of the replacement
					// (right gravity)
					position.setOffset(eventNewEndOffset);
				}
				
				// System.out.printf("  position %d offset:%d length:%d end:%d\n", i, position.getOffset(), position.getLength(), position.getOffset() + position.getLength());
			}
		} catch (BadPositionCategoryException e) {
			// ignore and return
		}
	}

	/**
	 * Returns the position category.
	 *
	 * @return the position category
	 */
	public String getCategory() {
		return fCategory;
	}
	
}
