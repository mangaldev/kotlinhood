package com.stock.kotlinhood.simulation.strategies

interface StrategyBase {
    /**
     * Use this method to initialize the required datastructures and initial state for the strategy
     * This is a chance to load trained models or load learned information from past data.
     */
    fun initialize()

    /**
     * This method will be called for every datapoint we have. If we have data that shows state
     * of a stock every 5 mins then this method will recieve all those data points. Use this method
     * to react to a datapoint. It can happen that strategy can choose to process different companies
     * stocks vs it can choose to process only Amazon stocks. then this method should filter other
     * stocks out.
     * TODO: this can be later made better by using a subscribe kind of model where a strategy can
     * subscribe for a company or frequency of data it wants to process.
     */
    fun processData(data:Map<String,String>)

    /**
     * Will be used by simulator to query each strategy instance about how much is their running profit.
     * This method has to consider the current transaction to be done before reporting this.
     * For example a single buy and sell strategy can happen to buy shares worth of x dollars, if this
     * strategy was queried at that exact moment it should report profit as 0.
     */
    fun reportProfit()

}
