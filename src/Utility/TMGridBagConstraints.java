package Utility;
/**
 *{@link tv.minato.gui.layout.TMGridBagLayout TMGridBagLayout}でレイアウトを指定するためのクラスです。
 *@since   1.8
 *@Version 1.00 2016/07/16 for Java 1.8
 *@author  1.00 Takayuki MINATO
 */
public class TMGridBagConstraints extends Object implements Cloneable {
	/**
	 *コンポーネントのx位置です。
	 */
	public int x;
	/**
	 *コンポーネントのy位置です。
	 */
	public int y;
	/**
	 *コンポーネントのグリッド幅です。
	 */
	public int width;
	/**
	 *コンポーネントのグリッド高です。
	 */
	public int height;

	/**
	 *本クラスを構築します。<br/>
	 *コンストラクタTMGridConstraint(0, 0, 1, 1)で構築するのと同じです。
	 *@see #TMGridBagConstraints(int, int, int, int)
	 */
	public TMGridBagConstraints(){
		this(0, 0, 1, 1);
	}

	/**
	 *本クラスを構築します。
	 *@param x コンポーネントのx位置
	 *@param y コンポーネントのy位置
	 *@param w コンポーネントのグリッド幅
	 *@param h コンポーネントのグリッド高
	 *@throws IllegalArgumentException 引数に不適切な値が指定された場合
	 */
	public TMGridBagConstraints(int x, int y, int w, int h) throws IllegalArgumentException {
		if(x<0 || y<0 || w<=0 || h<=0){
			throw new IllegalArgumentException("The argument value is invalid: x="+x+", y="+y+", w="+w+", h"+h);
		}
		this.x      = x;
		this.y      = y;
		this.width  = w;
		this.height = h;
	}

	/**
	 *本クラスのコピーを作成します。
	 *@return 本クラスのコピー
	 */
	@Override public Object clone () {
		try {
			return super.clone();
		}catch(final CloneNotSupportedException e){
			throw new RuntimeException("Failed to create clone");
		}
	}
}
