import sideImg from "../../assets/images/auth.jpg"
import ApiProvider from "@/components/ApiProvider";
import { githubIcon, googleIcon, checkIcon } from "@/assets/icons";
import Input from "@/components/Input";
import Button from "@/components/Button";
import { subscriptionBenefits } from "@/constants";

import AuthStep1 from "../../components/multi-step/AuthStep1";
import AuthStep2 from "../../components/multi-step/AuthStep2";
import AuthStep3 from "../../components/multi-step/AuthStep3";
import AuthStep4 from "../../components/multi-step/AuthStep4";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-dropdown"

import MultiStepAuth from "@/components/multi-step/MultiStepAuth";

const Auth = () => {


  return (
    <>
      <section className="flex lg:bg-linear-to-b from-[#141A17] to-[#121413]">
        <div className="lg:w-[45%] w-[100%] lg:relative absolute z-1">
          <div id="overlay" className="absolute top-0 bottom-0 right-0 left-0 bg-[#121413]/80 lg:bg-[#121413]/50 lg:rounded-r-[10px]"></div>
          <img src={sideImg} alt="image of a kayak" className="h-[100vh] w-full lg:rounded-r-[10px] object-cover object-center" />
        </div>

        <div className="padding-x lg:px-4 max-lg:pt-30 max-lg:h-[100vh] z-2 flex flex-1">
          <MultiStepAuth />
        </div>

      </section >
    </>

  );
}

export default Auth;
