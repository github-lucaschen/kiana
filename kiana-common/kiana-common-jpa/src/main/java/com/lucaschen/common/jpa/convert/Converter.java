package com.lucaschen.common.jpa.convert;

import java.util.List;

public interface Converter<S, T> {
    T toTarget(final S d);

    S toSource(final T p);

    List<T> toTargetList(final List<S> d);

    List<S> toSourceList(final List<T> p);
}
