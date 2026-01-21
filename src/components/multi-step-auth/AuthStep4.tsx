import { checkIcon } from "@/assets/icons";
import { useGSAP } from "@gsap/react";

const AuthStep4 = ({ tl }: { step: number, currentStep: number, setCurrentStep: React.Dispatch<React.SetStateAction<number>>, tl: GSAPTimeline }) => {

  setTimeout(() => {
    window.location.href = "/api/oauth2/authorization/github"

  }, 2 * 1000)


  useGSAP(() => {
    tl.fromTo("#success-icon", {
      yPercent: 20,
      opacity: 0,
      rotateZ: 100,
      scale: .7
    }, {
      yPercent: 0,
      opacity: 1,
      rotateZ: 0,
      scale: 1,
      duration: .4,
      delay: .5,
      ease: "power1.inOut"
    }, 0)
  }, [])

  return (
    <div className="step-content flex max-lg:pb-19 max-lg:pt-10 flex-col items-center flex-1 md:justify-center md:mt-[-200px] gap-5 lg:gap-5">
      <img id="success-icon" src={checkIcon} alt="success icon" className="h-25 w-25 lg:h-30 lg:w-30" />
      <div>
        <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Account created!</h1>
        <p className="text-desc font-mediumx font-inter mb-12 max-w-[470px] text-center">Redirecting you to homepage</p>
      </div>
    </div>
  );
}

export default AuthStep4;
