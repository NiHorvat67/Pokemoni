import ApiProvider from "@/components/ApiProvider";
import { githubIcon, googleIcon } from "@/assets/icons";



const AuthStep1 = ({ step, currentStep, setCurrentStep }: { step: number, currentStep: number, setCurrentStep: any }) => {


  return (
    <div className="step-content flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-200px] justify-between">
      <div>
        <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Tvoja avantura počinje ovdje</h1>
        <p className="text-desc font-inter md:mb-16 max-w-[470px] text-center">Pronađi savršenu sportsku opremu i kreni istraživati nove staze, planine i valove.</p>
      </div>
      <div className="flex flex-col gap-6">
        {/* <ApiProvider icon={googleIcon} text="Continue with Google" /> */}
        <ApiProvider icon={githubIcon} text="Continue with Github" />
      </div>
    </div>
  );
}

export default AuthStep1;
