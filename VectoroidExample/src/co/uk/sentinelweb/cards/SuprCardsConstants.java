package co.uk.sentinelweb.cards;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import co.uk.sentinelweb.views.draw.file.FileRepository;

public class SuprCardsConstants {

	static final String REPO_NAME = "XmasCards.sentinelweb";
	public static final String DRAW_LAYER_NAME = "Draw";
	public static final String CARD_NAME = "CardFront";
	public static final String PACKAGE = "co.uk.sentinelweb.cards";
	
	public static final String INTENT_ACTION_LOAD_FILE = "co.uk.sentinelweb.cards.LOAD_FILE";
	public static final String INTENT_PARAM_LOAD_FILE_SET = "set";
	public static final String INTENT_PARAM_LOAD_FILE_DRAWING = "drawing";
	
	private static FileRepository _fileRepo;
	
	public static FileRepository getFileRepo(Context c) {
		if (_fileRepo==null) {
			_fileRepo = FileRepository.getFileRepository( c, REPO_NAME);
		}
		return _fileRepo;
	}
	
	public static boolean appInstalled(Context c) {
		boolean paidVersionPresent = false;  
		try {
			PackageInfo p = c.getPackageManager().getPackageInfo(PACKAGE, 0); 
			if (p!=null) {
				paidVersionPresent=true;
			}
		} catch (NameNotFoundException e1) {
			
		}
		return paidVersionPresent;
	}

}
