package cl.niclabs.adkintunmobile.utils.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

public class DoughnutChart extends ImageView {

	private ArrayList<Integer> colors;
	private ArrayList<Float> values;

	private float diameter = 0;

	private float offset = 0;

	private Paint piePaint;
	private Paint transparentPaint;
	private RectF rectF;
	private Bitmap bitmap;
	private Canvas temp;

	public DoughnutChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		piePaint = new Paint();
		piePaint.setColor(Color.TRANSPARENT);
		piePaint.setFilterBitmap(true);
		piePaint.setDither(true);
		piePaint.setAntiAlias(true);

		rectF = new RectF();

		transparentPaint = new Paint();
		transparentPaint.setColor(Color.TRANSPARENT);
		transparentPaint.setFilterBitmap(true);
		transparentPaint.setDither(true);
		transparentPaint.setAntiAlias(true);
		transparentPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	}

	public void draw() {
		if (values == null || colors == null)
			return;

		rectF.set(0, 0, diameter, diameter);

		bitmap = Bitmap.createBitmap((int) diameter, (int) diameter,
				Bitmap.Config.ARGB_8888);
		temp = new Canvas(bitmap);

		float offset = this.offset;

		for (int i = 0; i < values.size(); i++) {

			piePaint.setColor(colors.get(i));

			temp.drawArc(rectF, offset, values.get(i), true, piePaint);

			offset += (values.get(i));
		}

		temp.drawCircle(diameter / 2, diameter / 2, diameter / 2.6f,
				transparentPaint);

		this.setImageBitmap(bitmap);
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	public ArrayList<Integer> getColors() {
		return colors;
	}

	public void setColors(ArrayList<Integer> colors) {
		this.colors = colors;
	}

	public ArrayList<Float> getValues() {
		return values;
	}

	public void setValues(ArrayList<Float> values) {
		this.values = values;
	}

	public void setDiameter(float diameter) {
		this.diameter = diameter;
	}

	public float getDiameter() {
		return diameter;
	}
}