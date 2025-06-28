import java.util.*;
import java.io.*;

public class Main {
	public static class SegmentTree {
		public static long[] tree;

		SegmentTree(int size, long[] a) {
			tree = new long[4 * size];
			build(1, 0, size - 1, a);
		}

		public long merge(long num1, long num2) {
			return Math.min(num1, num2);
		}

		public long base() {
			return Long.MAX_VALUE;
		}

		public void build(int x, int l, int r, long[] a) {
			if (l == r) {
				tree[x] = a[l];
				return;
			}
			int mid = l + (r - l) / 2;
			build(2 * x, l, mid, a);
			build(2 * x + 1, mid + 1, r, a);
			tree[x] = merge(tree[2 * x], tree[2 * x + 1]);
		}

		public void update(int x, int l, int r, int pos, long val) {
			if (l > pos || pos > r)
				return;
			if (l == pos && r == pos) {
				tree[x] = val;
				return;
			}
			int mid = l + (r - l) / 2;
			update(2 * x, l, mid, pos, val);
			update(2 * x + 1, mid + 1, r, pos, val);
			tree[x] = merge(tree[2 * x], tree[2 * x + 1]);
		}

		public long query(int x, int l, int r, int tl, int tr) {
			if (tl <= l && r <= tr)
				return tree[x]; // Completely included
			if (tr < l || r < tl)
				return base(); // Completely excluded
			// rest handle partially included cases
			int mid = l + (r - l) / 2;
			long a = query(2 * x, l, mid, tl, tr);
			long b = query(2 * x + 1, mid + 1, r, tl, tr);
			return merge(a, b);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		int n = sc.nextInt();
		int m = sc.nextInt();
		long[] a = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = sc.nextLong();
		}
		SegmentTree tree = new SegmentTree(a.length, a);
		for (int i = 0; i < m; i++) {
			int type = sc.nextInt();
			if (type == 1) {
				int ind = sc.nextInt();
				long val = sc.nextLong();
				tree.update(1, 0, n - 1, ind, val);
			} else {
				int l = sc.nextInt();
				int r = sc.nextInt() - 1;
				out.println(tree.query(1, 0, n - 1, l, r));
			}
		}
		out.flush();
	}
}
// l ....... pos....r
// l ....... tl.....tr....r-> Partially included
// ...tl.... l ... r...tr -> Completely included and returns total sum from l to
// r
// ...tl....tr....l.....r -> Competely excluded returns 0
// ...l ....r.....tl....tr -> Competely excluded returns 0
//
