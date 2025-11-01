import StepItem from "./StepItem";
import AuthStep1 from "./AuthStep1";
import AuthStep2 from "./AuthStep2";
import AuthStep3 from "./AuthStep3";
import AuthStep4 from "./AuthStep4";

import { useState } from "react";
import { useParams } from "react-router-dom";

const MultiStepAuth = () => {

  const { step } = useParams()

  const [currentStep, setCurrentStep] = useState(step == "2" ? 2 : 1)
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
