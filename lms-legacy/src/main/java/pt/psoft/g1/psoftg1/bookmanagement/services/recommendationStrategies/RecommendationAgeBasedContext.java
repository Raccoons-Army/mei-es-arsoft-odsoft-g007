package pt.psoft.g1.psoftg1.bookmanagement.services.recommendationStrategies;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Context class for the age-based recommendation strategies.
 */
@Service
public class RecommendationAgeBasedContext {

    // A TreeMap to hold age ranges and their corresponding strategies
    private final NavigableMap<Integer, RecommendationAgeBasedStrategy> ageStrategyMap = new TreeMap<>();

    public RecommendationAgeBasedContext(ChildrenStrategy childStrategy, JuvenileStrategy juvenileStrategy, AdultStrategy adultStrategy) {
        // Set up the strategy map based on the age ranges
        ageStrategyMap.put(0, childStrategy);    // For age < 10
        ageStrategyMap.put(10, juvenileStrategy);    // For age >= 10 and < 18
        ageStrategyMap.put(18, adultStrategy);   // For age >= 18
    }

    public RecommendationAgeBasedStrategy getStrategy(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        // Use floorEntry to find the largest key <= age
        return ageStrategyMap.floorEntry(age).getValue();
    }
}
