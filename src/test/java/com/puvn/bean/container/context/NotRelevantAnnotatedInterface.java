package com.puvn.bean.container.context;

// This class gets into a class map, but since this is not marked with any bean annotation, it should not be handled by
// dependency container, so it's placed here for such testing purposes
@SuppressWarnings(value = "anything")
public interface NotRelevantAnnotatedInterface {
}
