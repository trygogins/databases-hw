package ru.hsestudy.databases.web.model;

/**
 * @author georgii
 * @since 6/14/14
 */
public class Pair {

    private User left;
    private User right;

    public Pair(User left, User right) {
        this.left = left;
        this.right = right;
    }

    public User getLeft() {
        return left;
    }

    public void setLeft(User left) {
        this.left = left;
    }

    public User getRight() {
        return right;
    }

    public void setRight(User right) {
        this.right = right;
    }
}
