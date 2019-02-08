package Utility;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *{@link java.awt.GridLayout}を改良したレイアウトマネージャです。
 *@since   1.8
 *@Version 1.00 2016/07/16 for Java 1.8
 *@Version 1.01 2016/07/17 for Java 1.8 Change two of List to one of Map.
 *@author  0.50-1.01 Takayuki MINATO
 */
public final class TMGridBagLayout extends Object implements LayoutManager2, Serializable {
	private static final long serialVersionUID = 6450251806839731638L;
	private final Map<Component, TMGridBagConstraints> constraintsMap = new HashMap<>();
	private boolean calculated = false;
	private int[] gridWidths;
	private int[] gridHeights;
	private int resizableGridX = -1;
	private int resizableGridY = -1;
	private int hGap       = 0;
	private int vGap       = 0;

	/**
	 *本クラスを構築します。
	 *@param width x軸方向のグリッド数(0以上の数値)
	 *@param height y軸方向のグリッド数(0以上の数値)
	 *@throws IllegalArgumentException 引数の値に誤りがあるとき
	 */
	public TMGridBagLayout(int width, int height){
		this(width, height, -1, -1);
	}

	/**
	 *本クラスを構築します。
	 *@param width x軸方向のグリッド数(0以上の数値)
	 *@param height y軸方向のグリッド数(0以上の数値)
	 *@param resizableX 親コンテナの幅に応じて幅を伸縮するx軸方向のグリッド位置(0未満の場合伸縮しない)
	 *@param resizableY 親コンテナの高さに応じて高さを伸縮するy軸方向のグリッド位置(0未満の場合伸縮しない)
	 *@throws IllegalArgumentException 引数の値に誤りがあるとき
	 */
	public TMGridBagLayout(int width, int height, int resizableX, int resizableY) throws IllegalArgumentException {
		if(width <= 0 || height <= 0){
			throw new IllegalArgumentException("The argument value is invalid: width="+width+", height="+height);
		}
		this.gridWidths  = new int[width];
		this.gridHeights = new int[height];
		this.setResizableX(resizableX);
		this.setResizableY(resizableY);
	}

	/**
	 *コンテナのサイズに応じて、サイズ変更を行うx軸方向のグリッドを取得します。
	 *@return x軸方向のグリッド(デフォルトは、どのコンポーネントもサイズ変更しないことを示す-1)
	 */
	public int getResizableX(){
		return this.resizableGridX;
	}

	/**
	 *コンテナのサイズに応じて、サイズ変更を行うx軸方向のグリッドを指定します。
	 *@param resizableX x軸方向のグリッド(0未満の場合、どのコンポーネントもサイズ変更を行わない)
	 *@throws IllegalArgumentException 引数の値に誤りがあるとき
	 */
	public void setResizableX(int resizableX) throws IllegalArgumentException {
		if(resizableX >= this.gridWidths.length){
			throw new IllegalArgumentException("The argument value is invalid: resizableX="+resizableX);
		}
		this.resizableGridX = resizableX;
	}

	/**
	 *コンテナのサイズに応じて、サイズ変更を行うy軸方向のグリッドを取得します。
	 *@return y軸方向のグリッド(デフォルトは、どのコンポーネントもサイズ変更しないことを示す-1)
	 */
	public int getResizableY(){
		return this.resizableGridY;
	}

	/**
	 *コンテナのサイズに応じて、サイズ変更を行うy軸方向のグリッドを指定します。
	 *@param resizableY y軸方向のグリッド(0未満の場合、どのコンポーネントもサイズ変更を行わない)
	 *@throws IllegalArgumentException 引数の値に誤りがあるとき
	 */
	public void setResizableY(int resizableY) throws IllegalArgumentException {
		if(resizableY >= this.gridHeights.length){
			throw new IllegalArgumentException("The argument value is invalid: resizableY="+resizableY);
		}
		this.resizableGridY = resizableY;
	}

	/**
	 *コンポーネント間のx軸方向のスペースを取得します。
	 *@return コンポーネント間のスペース
	 */
	public int getHgap(){
		return this.hGap;
	}

	/**
	 *コンポーネント間のx軸方向のスペースを設定します。
	 *@param gap コンポーネント間のスペース
	 */
	public void setHgap(int gap){
		this.hGap = gap;
	}

	/**
	 *コンポーネント間のx軸方向のスペースを取得します。
	 *@return コンポーネント間のスペース
	 */
	public int getVgap(){
		return this.vGap;
	}

	/**
	 *コンポーネント間のy軸方向のスペースを設定します。
	 *@param gap コンポーネント間のスペース
	 */
	public void setVgap(int gap){
		this.vGap = gap;
	}

	/**
	 *コンポーネントを追加するときに呼ばれます。
	 *@param name コンポーネントに関連づけられた文字列
	 *@param comp 追加されるコンポーネント
	 */
	@Override
	public void addLayoutComponent(String name, Component comp){
		//NOP
	}

	/**
	 *コンポーネントを追加するときに呼ばれます。
	 *@param comp 追加されるコンポーネント
	 *@param constraints コンポーネントに関連づけられたコンストレイント({@link TMGridBagConstraints}を利用)
	 *@throws IllegalArgumentException 引数constraintsの値に誤りがあるとき
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints){
		TMGridBagConstraints gc = (TMGridBagConstraints)constraints;
		if(gc.x+gc.width>this.gridWidths.length || gc.y+gc.height>this.gridHeights.length){
			throw new IllegalArgumentException("The Value of constraints is invalid: constraints="+gc);
		}
		gc = (TMGridBagConstraints)gc.clone();
		constraintsMap.put(comp, gc);
	}

	/**
	 *コンポーネントを削除するときに呼ばれます。
	 *@param comp 削除されるコンポーネント
	 */
	@Override
	public void removeLayoutComponent(Component comp){
		constraintsMap.remove(comp);
	}

	/**
	 *x軸方向の配置方法を取得します。
	 *@param target コンテナ
	 *@return x軸の配置方法(0～1の間の数値)
	 */
	@Override
	public float getLayoutAlignmentX(Container target){
		return 0L;
	}

	/**
	 *y軸方向の配置方法を取得します。
	 *@param target コンテナ
	 *@return y軸の配置方法(0～1の間の数値)
	 */
	@Override
	public float getLayoutAlignmentY(Container target){
		return 0L;
	}

	/**
	 *コンテナの最小サイズを取得します。
	 *@param target コンテナ
	 *@return コンテナの最小サイズ
	 */
	@Override
	public Dimension minimumLayoutSize(Container target){
		return this.preferredLayoutSize(target);
	}

	/**
	 *コンテナの最大サイズを計算します。
	 *@param target コンテナ
	 *@return コンテナの最大サイズ
	 */
	@Override
	public Dimension maximumLayoutSize(Container target){
		return this.preferredLayoutSize(target);
	}

	/**
	 *コンテナの推奨サイズを取得します。
	 *@param target コンテナ
	 *@return コンテナの推奨サイズ
	 */
	@Override
	public Dimension preferredLayoutSize(Container target){
		//コンポーネントのサイズを求める
		calculateSize();
		//幅と高さ等からサイズを算出
		final int[] widths = this.gridWidths;
		final int[] heights = this.gridHeights;
		final int w = totalInteger(widths,  0, widths.length);
		final int h = totalInteger(heights, 0, heights.length);
		final int hg= this.hGap * (widths.length-1);
		final int vg= this.vGap * (heights.length-1);
		final Insets in = target.getInsets();
		return new Dimension(in.left+in.right+w+hg, in.top+in.bottom+h+vg);
	}

	/**
	 *コンポーネントをレイアウトするときに呼ばれます。
	 *@param target コンテナ
	 */
	@Override
	public void layoutContainer(Container target){
		final Map<Component, TMGridBagConstraints> map = this.constraintsMap;
		//コンポーネントがなければ、レイアウトしない
		if(map.isEmpty()){
			return;
		}
		//コンポーネントのサイズを求める
		calculateSize();
		//x,y,width,heightを算出
		final int[] widths = this.gridWidths;
		final int[] heights = this.gridHeights;
		final int hg = this.hGap;
		final int vg = this.vGap;

		final Insets in = target.getInsets();
		final int insetsWidth  = in.left + in.right;
		final int insetsHeight = in.top  + in.bottom;
		final Dimension size = target.getSize();
		final int hs= hg * (widths.length-1);
		final int vs= vg * (heights.length-1);
		final int totalWidth  = totalInteger(widths,  0, widths.length)+insetsWidth+hs;
		final int totalHeight = totalInteger(heights, 0, heights.length)+insetsHeight+vs;
		final int dw = (size.width - totalWidth);
		final int dh = (size.height - totalHeight);

		//コンテナの幅がpreferredSizeより大きいか小さい場合、特定の行列のサイズを調整する
		final int resizableX = this.resizableGridX;
		final int resizableY = this.resizableGridY;
		if(resizableX >= 0){
			widths[resizableX] += dw;
			if(widths[resizableX] < 0){
				widths[resizableX] = 0;
			}
		}
		if(resizableY >= 0){
			heights[resizableY] += dh;
			if(heights[resizableY] < 0){
				heights[resizableY] = 0;
			}
		}
		map.forEach((comp, gc)->{
			int x = totalInteger(widths,  0, gc.x) + hg*gc.x;
			int y = totalInteger(heights, 0, gc.y) + vg*gc.y;
			int w = totalInteger(widths,  gc.x, gc.width);
			int h = totalInteger(heights, gc.y, gc.height);
			w += hg * (gc.width-1);
			h += vg * (gc.height-1);
			comp.setBounds(in.left+x, in.top+y, w, h);
		});
	}

	/**
	 *レイアウトを無効にします。
	 *@param target コンテナ
	 */
	@Override
	public void invalidateLayout(Container target){
		this.calculated = false;
	}

	/**
	 *サイズを計算する
	 */
	private void calculateSize(){
		if(this.calculated == true){
			return;
		}
		this.calculated = true;

		//gc.width、gc.heightの値が1であれば、コンポーネントの最大のpreferredSizeを元に決定
		final int[] widths = this.gridWidths;
		final int[] heights = this.gridHeights;
		final Map<Component, TMGridBagConstraints> resizeComponents = new HashMap<>();
		constraintsMap.forEach((comp, gc)->{
			if(gc.width == 1){
				widths[gc.x] = Math.max(widths[gc.x], comp.getPreferredSize().width);
			}
			if(gc.height == 1){
				heights[gc.y] = Math.max(heights[gc.y], comp.getPreferredSize().height);
			}
			if(gc.width > 1 || gc.height > 1){	//width、heightが2以上は後で計算する
				resizeComponents.put(comp, gc);
			}
		});
		//gc.width、gc.heightの値が2以上のコンポーネントに合わせてサイズを調整
		resizeComponents.forEach((comp, gc)->{
			if(gc.width > 1){
				final int p = comp.getPreferredSize().width;
				final int a = totalInteger(widths, gc.x, gc.width);
				if(a < p){
					widths[gc.x+gc.width-1] += (p-a);
				}
			}
			if(gc.height > 1){
				final int p = comp.getPreferredSize().height;
				final int a = totalInteger(heights, gc.y, gc.height);
				if(a < p){
					heights[gc.y+gc.height-1] += (p-a);
				}
			}
		});
	}

	/**
	 *int配列の合計を計算
	 */
	private static int totalInteger(int[] v, int from, int length){
		return IntStream.range(from, from+length).map(i->v[i]).sum();
	}
}