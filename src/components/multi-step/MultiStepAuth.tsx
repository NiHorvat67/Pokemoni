import StepItem from "./StepItem";
import AuthStep1 from "./AuthStep1";
import AuthStep2 from "./AuthStep2";
import AuthStep3 from "./AuthStep3";
import AuthStep4 from "./AuthStep4";

import { useState } from "react";

const MultiStepAuth = () => {

  const [currentStep, setCurrentStep] = useState(1)
  const stepComponents = [AuthStep1, AuthStep2, AuthStep3, AuthStep4]

  return (
    <>
      {stepComponents.map((stepComponent, index) => (
        <StepItem key={index} step={index + 1} setCurrentStep={setCurrentStep} currentStep={currentStep} StepContent={stepComponent} />
      ))
      }
    </>

  );
}

export default MultiStepAuth;
