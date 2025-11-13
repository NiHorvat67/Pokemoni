import { subscriptionBenefits } from "@/constants";
import { checkIcon } from "@/assets/icons";
import Button from "@/components/Button";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";
import useCreateAccountContext from "@/hooks/useCreateAccountContext";

const AuthStep3 = ({ step, setCurrentStep }: { step: number, currentStep: number, setCurrentStep: any }) => {

  const { userData, setUserDataField } = useCreateAccountContext()

  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "post",
        url: "/api/accounts/create/trader",
        data: {
          userFirstName: userData.userFirstName,
          userLastName: userData.userLastName,
          userLocation: userData.userLocation,
          accountRole: userData.accountRole,
          userContact: userData.userContact,

        }
      })
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    }, onSuccess: (redirectUrl) => {
      window.location.href = redirectUrl;
    }

  })

  const paymentOnClick = () => {
    console.log(userData)
    mutate()
  }

  return (
    <div className="step-content flex max-lg:pb-19 flex-col items-center flex-1 md:justify-center md:mt-[-100px]">
      <div>
        <h1 id="heading" className="font-red-hat mb-5 text-white text-[28px] sm:text-[32px] font-medium text-center">Activate Subscription</h1>
        <p className="text-desc font-inter mb-16 max-w-[470px] text-center">Subscribe now to list your equipment on GearShare and reach customers ready to rent your gear.</p>
      </div>

      <div className="flex flex-col gap-8">
        <div className="flex flex-col gap-4">
          {subscriptionBenefits.map((benefit) => (
            <div key={benefit} className="flex items-center gap-2 text-white font-inter font-medium">
              <img src={checkIcon} alt="checkmark icon" />
              {benefit}
            </div>
          ))}
        </div>
        <p className="font-inter font-semibold text-white text-[24px]">100€ <span className="font-normal text-base">/ YEAR</span></p>
        <Button text="Proceed to Payment" icon={true} onClick={paymentOnClick} />
      </div>
    </div>
  );
}

export default AuthStep3;
