
import gsap from "gsap";
import { useGSAP } from "@gsap/react";

const StepItem = ({ step, currentStep, setCurrentStep, StepContent }: { step: number, currentStep: number, StepContent: any, setCurrentStep: React.Dispatch<React.SetStateAction<number>> }) => {

  const tl = gsap.timeline()

  useGSAP(() => {
    if (currentStep === step) {
      tl.from(".step-content", {
        opacity: 0,
        ease: "power1.inOut",
        duration: .75,
        x: -35
      }, 0)
    }
  }, [currentStep])

  return (
    <>
      {step === currentStep &&
        <>
          {<StepContent step={step} tl={tl} currentStep={currentStep} setCurrentStep={setCurrentStep} />}
        </>
      }
    </>
  );
}

export default StepItem;
