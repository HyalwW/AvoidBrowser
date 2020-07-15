package com.hyaline.avoidbrowser.utils;

public class BindingCommand<T> {

    BindingConsumer<T> consumer;

    BindingAction action;

    public BindingCommand(BindingConsumer<T> consumer) {
        this.consumer = consumer;
    }

    public BindingCommand(BindingAction action) {
        this.action = action;
    }

    public void execute() {
        if (action == null) {
            return;
        }
        action.call();
    }

    public void execute(T t) {
        if (consumer == null) {
            return;
        }
        consumer.call(t);
    }

    public interface BindingConsumer<T> {
        void call(T t);
    }

    public interface BindingAction {
        void call();
    }

}
