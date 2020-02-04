package com.olekdia.mvp

/**
 * Base interface that define component of the framework,
 * it has only one getter of componentId.
 * All framework entities should implement this interface.
 */
interface IComponent {
    val componentId: String
}