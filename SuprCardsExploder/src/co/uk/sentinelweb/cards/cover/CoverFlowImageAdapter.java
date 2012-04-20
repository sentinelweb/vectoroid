package co.uk.sentinelweb.cards.cover;
/*
SuprCards Explodr for Android
Copyright (C) 2010-12 Sentinel Web Technologies Ltd
All rights reserved.
 
This software is made available under a Dual Licence:
 
Use is permitted under LGPL terms for Non-commercial projects
  
see: LICENCE.txt for more information

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Sentinel Web Technologies Ltd. nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SENTINEL WEB TECHNOLOGIES LTD BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import co.uk.sentinelweb.cards.SuprCardsConstants;
import co.uk.sentinelweb.example.cards.explode.R;
import co.uk.sentinelweb.views.draw.file.FileRepository;
import co.uk.sentinelweb.views.draw.file.SaveFile;

public class CoverFlowImageAdapter extends BaseAdapter implements ListAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	Paint borderPaint;
	// private FileInputStream fis;
	/*
	 * private Integer[] mImageIds = { R.drawable.i_align_dist_bedge_v,
	 * R.drawable.i_align_dist_ctr_h, R.drawable.i_align_dist_ctr_v,
	 * R.drawable.i_align_dist_l_edge_h, R.drawable.i_align_dist_redge_h,
	 * R.drawable.i_align_dist_tedge_v, R.drawable.i_align_bottom,
	 * R.drawable.i_align_left, R.drawable.i_align_right };
	 */
	public ArrayList<SaveFile> mImagesFile;

	private ArrayList<ImageView> mImages;
	boolean _isCover ;
	public int _height = 450;
	private int _selectedPosition;
	public CoverFlowImageAdapter(Context c,FileRepository fr) {
		mContext = c;
		borderPaint = new Paint();
		borderPaint.setColor(Color.DKGRAY);
		borderPaint.setStrokeWidth(2);
		borderPaint.setStyle(Style.STROKE);
		_isCover = true;//((CoverFlowLoaderActivity)mContext)._isCover;
		
		refresh(fr);
	}

	public ImageView createReflectedImage(int pos) {
		// The gap we want between the reflection and the original image
		final int reflectionGap = 4;
		// for (SaveFile imageId : mImagesFile) {
		ImageView imageView = new ImageView(mContext);
		if (mImagesFile.get(pos).getSet().getDrawingIDs().size()>0) {
			
			Bitmap originalImage = null;
			try {
				originalImage = BitmapFactory.decodeFile(mImagesFile.get(pos).getPreviewFile().getAbsolutePath());
			} catch (OutOfMemoryError e) {
				System.gc();
			}
			if (originalImage != null) {
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();
	
				// This will not scale but will flip on the Y axis
				Matrix matrix = new Matrix();
				matrix.preScale(1, -1);
	
				// Create a Bitmap with the flip matrix applied to it.
				// We only want the bottom half of the image
				try {
					Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,	height / 2, width, height / 2, matrix, false);
		
					// Create a new bitmap with same width but taller to fit reflection
					Bitmap bitmapWithReflection = Bitmap.createBitmap(width,(height + height / 2), Config.ARGB_8888);
		
					// Create a new Canvas with the bitmap that's big enough for
					// the image plus gap plus reflection
					Canvas canvas = new Canvas(bitmapWithReflection);
					// Draw in the original image
					canvas.drawBitmap(originalImage, 0, 0, null);
					canvas.drawRect(0, 0, originalImage.getWidth(),	originalImage.getHeight(), borderPaint);
					// Draw in the gap
					Paint deafaultPaint = new Paint();
					canvas.drawRect(0, height, width, height + reflectionGap,deafaultPaint);
					// Draw in the reflection
					canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		
					// Create a shader that is a linear gradient that covers the
					// reflection
					Paint paint = new Paint();
					LinearGradient shader = new LinearGradient(0,
							originalImage.getHeight(), 0,
							bitmapWithReflection.getHeight() + reflectionGap,
							0x30ffffff, 0x00ffffff, TileMode.CLAMP);
					// Set the paint to use this shader (linear gradient)
					paint.setShader(shader);
					// Set the Transfer mode to be porter duff and destination in
					paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
					// Draw a rectangle using the paint with our linear gradient
					canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()	+ reflectionGap, paint);
					imageView.setImageBitmap(bitmapWithReflection);
				} catch (OutOfMemoryError e) {
					originalImage=null;
					System.gc();
				}
			} 
			if (originalImage == null) {
				imageView.setImageResource(R.drawable.i_help);
			}
		} else {
			imageView.setImageResource(R.drawable.i_help);
		}
		if (_isCover) {
			imageView.setLayoutParams(new CoverFlow.LayoutParams((int)(_height/1.5f),(int)(_height)));
		} else {
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(layoutParams);
			imageView.setPadding(5, 5, 5, 5);
		}
		imageView.setScaleType(ScaleType.FIT_CENTER);
		// mImages[index++] = imageView;

		// }
		return imageView;
	}

	public int getCount() {
		return mImagesFile.size();
	}

	public Object getItem(int position) {
		if (mImagesFile.size()>position) {
			return mImagesFile.get(position);
		} else return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// Use this code if you want to load from resources
		ImageView imageView = mImages.get(position);
		if (imageView == null) {
			imageView = createReflectedImage(position);
			mImages.set(position, imageView);
			/*
			 * ImageView i = new ImageView(mContext); Bitmap previewBitmap =
			 * mImagesFile.get(position).getPreviewBitmap();
			 * i.setImageBitmap(previewBitmap); i.setLayoutParams(new
			 * CoverFlow.LayoutParams(254,254));
			 * i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			 * i.setBackgroundColor(Color.DKGRAY); i.setPadding(2, 2, 2, 2);
			 * //Make sure we set anti-aliasing otherwise we get jaggies
			 * BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			 * drawable.setAntiAlias(true); return i;
			 */
		}
		if (position==_selectedPosition && !_isCover) {
			imageView.setBackgroundColor(Color.argb(255, 0, 128, 192));
		} else {
			imageView.setBackgroundColor(0);
		}
		if (!_isCover) {
			imageView.setPadding(5, 5, 5, 5);
		} else {
			imageView.setPadding(0,0,0,0);
		}
		return imageView;
	}
	public void setSelectedItemPosition(int position) {
        _selectedPosition = position;
    }
	/**
	 * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
	 * the center.
	 */
	public float getScale(boolean focused, int offset) {
		/* Formula: 1 / (2 ^ offset) */
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

	public void refresh(FileRepository fr) {
		
		mImagesFile =fr.getFiles(
				this.mContext);
		// mImages = new ImageView[mImagesFile.size()];
		ArrayList<SaveFile> copy = new ArrayList<SaveFile>(mImagesFile);
		Collections.sort(mImagesFile,new Comparator<SaveFile>(){
				@Override
				public int compare(SaveFile lhs, SaveFile rhs) {
					File lhf = lhs.getDrawingFile(SuprCardsConstants.CARD_NAME);
					File rhf = rhs.getDrawingFile(SuprCardsConstants.CARD_NAME);
					if (lhf.exists()&&!rhf.exists()) {
						return -1;
					} else if (!lhf.exists() && rhf.exists()) {
						return 1;
					} else {
						return (int)(rhf.lastModified()-lhf.lastModified());
					}
				}
			}
		);
		mImages = new ArrayList<ImageView>();
		for (int i = 0; i < mImagesFile.size(); i++) {
			mImages.add(null);
		}
		notifyDataSetChanged();
	}

	public void remove(Object o) {
		int pos = mImagesFile.indexOf(o);
		mImagesFile.remove(pos);
		mImages.remove(pos);
		notifyDataSetChanged();
	}
}