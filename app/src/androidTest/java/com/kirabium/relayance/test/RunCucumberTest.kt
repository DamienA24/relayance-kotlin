package com.kirabium.relayance.test

import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    glue = ["com.kirabium.relayance.cucumber"],
    features = ["features"]
)
class RunCucumberTest
