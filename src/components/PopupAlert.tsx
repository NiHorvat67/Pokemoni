import { AlertCircleIcon } from "lucide-react"
import {
  Alert,
  AlertDescription,
  AlertTitle,
} from "@/components/ui/alert"

import gsap from "gsap";
import { useGSAP } from "@gsap/react";

const PopupAlert = ({ errors }: { errors: string[] }) => {

  useGSAP(() => {
    gsap.from(".error-container", {
      opacity: 0,
      y: 20,
      duration: .5,
      ease: "power1.inOut"
    })
    gsap.to(".error-container", {
      opacity: 0,
      y: 20,
      duration: .5,
      delay: 7,
      ease: "power1.inOut"
    })
  }, [errors])

  return (

    <div className="error-container w-full p-6 flex justify-center absolute bottom-0 right-0">
      <div className="w-full max-w-lg">
        <Alert variant="destructive">
          <AlertCircleIcon />
          <AlertTitle>Please fix the following errors</AlertTitle>
          <AlertDescription>
            <ul className="list-disc list-inside space-y-1 mt-2">
              {errors.map((error: string, index: number) => (
                <li key={error}>{error}</li>

              ))}
            </ul>
          </AlertDescription>
        </Alert>
      </div>
    </div>

  );
}

export default PopupAlert;
