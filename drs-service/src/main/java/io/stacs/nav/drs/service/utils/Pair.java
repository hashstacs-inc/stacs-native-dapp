package io.stacs.nav.drs.service.utils;

import lombok.Getter;

/**
 * @author dekuofa <br>
 * @date 2019-11-20 <br>
 */
@Getter public class Pair<L, R> {
    private L left;
    private R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public Pair<L, R> left(L l) {
        this.left = l;
        return this;
    }

    public Pair<L, R> right(R r) {
        this.right = r;
        return this;
    }

    @Override public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        Pair pair = (Pair)obj;
        return this.left.equals(pair.left) && this.right.equals(pair);
    }

    @Override public int hashCode() {
        return (left == null ? 0 : left.hashCode()) ^ (right == null ? 0 : right.hashCode());
    }
}
