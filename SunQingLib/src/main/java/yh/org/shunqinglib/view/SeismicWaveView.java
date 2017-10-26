package yh.org.shunqinglib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: SeismicWaveView
 * @Description: yh
 * @author 地图动画
 * @date 2016-3-17 下午2:18:50
 * 
 */
public class SeismicWaveView extends View
{

    private Paint paint,paintBitmap;
    private int maxWidth = 200;
    private boolean isStarting = true;
    private List<String> alphaList = new ArrayList<String>();
    private List<String> startWidthList = new ArrayList<String>();
    private Point ponit;
    private Bitmap bitmap;

    public SeismicWaveView(Context context) {
        super(context);
        init(context);
    }

    public void setPaint(Point ponit) {
        this.ponit = ponit;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public SeismicWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeismicWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paintBitmap = new Paint();
        // (0, 127, 255
        paint.setColor(Color.rgb(0, 127, 255));// 此处颜色可以改为自己喜欢的
        alphaList.add("200");// 圆心的不透明度
        startWidthList.add("0");
        // img_my.setVisibility(View.GONE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != this.ponit && null != bitmap) {

            setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
            // 依次绘制 同心圆
            for (int i = 0; i < alphaList.size(); i++) {
                int alpha = Integer.parseInt(alphaList.get(i));
                int startWidth = Integer.parseInt(startWidthList.get(i));
                paint.setAlpha(alpha);
                canvas.drawCircle(this.ponit.x, this.ponit.y, startWidth, paint);
                // 同心圆扩散
                if (isStarting && alpha > 0 && startWidth < maxWidth) {
                    alphaList.set(i, (alpha - 1) + "");
                    startWidthList.set(i, (startWidth + 1) + "");
                }
            }
            if (isStarting
                    && Integer.parseInt(startWidthList.get(startWidthList
                            .size() - 1)) == maxWidth / 5) {
                alphaList.add("200");
                startWidthList.add("0");
            }
            // 同心圆数量达到6个，删除最外层圆
            if (isStarting && startWidthList.size() == 5) {
                startWidthList.remove(0);
                alphaList.remove(0);
            }
            int bW = bitmap.getScaledWidth(bitmap.getDensity());
            int bH = bitmap.getScaledHeight(bitmap.getDensity());
            canvas.drawBitmap(bitmap, this.ponit.x - bW / 2, this.ponit.y - bH
                    / 2, paintBitmap);
        }
        // 刷新界面
        invalidate();
    }

    // 地震波开始/继续进行
    public void start() {
        isStarting = true;
    }

    // 地震波暂停
    public void stop() {
        isStarting = false;
    }

    public boolean isStarting() {
        return isStarting;
    }

}
