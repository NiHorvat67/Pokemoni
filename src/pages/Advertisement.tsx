import { locationIcon } from "@/assets/icons";
import Button from "@/components/Button";
import { useQuery, useMutation } from "@tanstack/react-query";
import axios from "axios";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import useAuthContext from "@/hooks/useAuthContext";
import { editIcon, deleteIcon } from "@/assets/icons";
import { useNavigate } from "react-router-dom";
import Calendar14 from "@/components/calendar-14";
import PopupAlert from "@/components/PopupAlert";
import { numberOfDays } from "@/lib/utils";
import StepItem from "@/components/multi-step-auth/StepItem";
import { CreditCard } from "@/components/CreditCard";

const Advertisement = () => {
  const navigate = useNavigate()
  const { advertisementId } = useParams()
  const [currentUserIsOwner, setCurrentUserIsOwner] = useState<boolean>(false)
  const [dateRange, setDateRange] = useState<{ from: Date | undefined, to: Date | undefined }>({ from: undefined, to: undefined })
  const { user } = useAuthContext()
  const [errors, setErrors] = useState<any>([])
  const [currentStep, setCurrentStep] = useState(1)


  const { data: advertisementData } = useQuery({
    queryKey: ["advertisementData"],
    queryFn: async () => {
      return axios
        .get(`/api/advertisements/${advertisementId}`)
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
          if (err.response.status === 404) {
            navigate("/error")
          }
        })
    }
  })

  const { data: reservationPeriods } = useQuery({
    queryKey: ["reservationPeriods"],
    queryFn: async () => {
      return axios
        .get(`/api/reservations/advertisement_intervals/${advertisementId}`)
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
          if (err.response.status === 404) {
            navigate("/error")
          }
        })
    }
  })

  const { mutate: deleteAdvertisement } = useMutation({
    mutationFn: async () => {
      return axios.post(`/api/advertisements/delete/${advertisementData?.advertisementId}`)
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err));
    },
    onSuccess: () => {
      navigate(`/profile/${advertisementData?.trader.accountId}`)
    }
  });

  const { mutate: makeReservation } = useMutation({
    mutationFn: async () => {
      return axios({
        method: "POST",
        url: `/api/reservations/create`,
        data: {
          reservationStart: dateRange.from!.toLocaleDateString("en-CA"),
          reservationEnd: dateRange.to!.toLocaleDateString("en-CA"),
          advertisementId: advertisementData?.advertisementId,
          buyerId: user.accountId
        }
      })
        .then(res => {
          navigate(`/profile/${user.accountId}`)
          return res.data
        })
        .catch(err => console.log(err));
    },
    onSuccess: () => {
      navigate("/")
    }
  });


  function processPayment() {
    makeReservation()
  }

  function proceedToPayment() {
    setErrors([])
    if (!user) {
      navigate("/auth")
    }
    if (datesSelected()) {
      setCurrentStep(2)
    } else {
      setErrors((prev: string[]) => [...prev, "Please select start and end dates."])
      setTimeout(() => {
        setErrors([])
      }, 7 * 1000)
    }
  }

  function datesSelected() {
    return dateRange.from !== undefined && dateRange.to !== undefined
  }


  function deleteOnClick() {
    deleteAdvertisement()
  }

  useEffect(() => {
    setCurrentUserIsOwner(advertisementData?.trader.accountId === user?.accountId && user?.accountId !== undefined)
  }, [advertisementData])

  return (

    <section className="padding-x padding-t padding-b">
      <section className="max-container">
        <div className="flex gap-6 lg:gap-20 max-lg:flex-col">
          {/* <img src={`/api/advertisements/images/load/${advertisementData?.advertisementId}`} alt="product image" className="lg:w-2/5 object-cover object-center rounded-[8px] w-full max-h-[300px] lg:max-h-[400px]" /> */}
          <img src={`https://images.unsplash.com/photo-1449426468159-d96dbf08f19f?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8bW90b3JiaWtlfGVufDB8fDB8fHww`} alt="product image" className="lg:w-2/5 object-cover object-center rounded-[8px] w-full max-h-[200px] md:max-h-[300px] lg:max-h-[400px]" />

          <StepItem step={1} setCurrentStep={setCurrentStep} currentStep={currentStep} StepContent={
            () => (
              <>
                <div className="step-content flex flex-col items-start gap-10 lg:gap-12 flex-1">
                  <div className="flex items-start gap-1 flex-col w-full">
                    <div className="flex gap-2 items-center mb-3">
                      {currentUserIsOwner &&
                        <>
                          <a
                            href={`/ `}
                            className="rounded-[8px] hover:shadow-[0_0_15px_#E51739] transition-shadow duration-300 pl-2 pr-2.5 py-1.25 bg-primary text-black flex items-center gap-1 max-sm:text-[14px]"
                          >
                            <img src={editIcon} alt="edit icon" className="w-4 h-4" />
                            Edit
                          </a>
                          <span
                            onClick={deleteOnClick}
                            className="cursor-pointer selection:bg-transparent rounded-[8px] hover:shadow-[0_0_15px_#FF264A] transition-shadow duration-300 pl-2 pr-2.5 py-1.25 bg-[#FF1F44] text-black flex items-center gap-1 max-sm:text-[14px]"
                          >
                            <img src={deleteIcon} alt="edit icon" className="w-4 h-4" />
                            Delete
                          </span>
                        </>
                      }

                    </div>
                    <p className="text-primary bg-[#102B19] rounded-[8px] px-2.5 py-1.25 text-[13px] font-inter">{advertisementData?.itemType.itemtypeName}</p>
                    <h1 className="font-inter text-[24px] sm:text-[32px] font-medium text-white mr-8 mb-1">{advertisementData?.itemName}</h1>
                    <p className="font-inter text-white text-[14px]">by <a className="font-medium" href={`/profile/${advertisementData?.trader.accountId}`}>{advertisementData?.trader.userFirstName} {advertisementData?.trader.userLastName}</a></p>
                  </div>
                  <p className="font-inter text-white text-[16px] max-w-[700px]"> {advertisementData?.itemDescription}</p>

                  <div className="text-white font-inter flex flex-col gap-5">
                    <div className="flex items-start max-w-[700px] gap-1">
                      <img src={locationIcon} alt="location icon" />
                      <p>Preuzimanje — {advertisementData?.advertisementLocationTakeover}</p>
                    </div>
                    <div className="flex items-start max-w-[700px] gap-1">
                      <img src={locationIcon} alt="location icon" />
                      <p>Povrat — {advertisementData?.advertisementLocationReturn}</p>
                    </div>
                  </div>

                  {reservationPeriods !== undefined &&
                    <Calendar14 range={dateRange}
                      setRange={setDateRange}
                      advertisementStart={advertisementData?.advertisementStart}
                      advertisementEnd={advertisementData?.advertisementEnd}
                      reservationPeriods={reservationPeriods}
                    />
                  }

                  <div className="flex font-inter items-center gap-11">
                    <div className="relative flex flex-col items-start flex-1 justify-start text-white min-w-[100px]">
                      <h1 className="font-medium text-[32px]">
                        {advertisementData?.advertisementPrice}€
                        <span className="text-sm font-normal">
                          &nbsp;/day
                        </span>

                      </h1>
                      <p className="text-desc text-sm">Kaucija {advertisementData?.advertisementDeposit}€</p>
                      {numberOfDays(dateRange.from!, dateRange.to!) !== 0 &&
                        <p className="text-desc text-sm absolute bottom-[-22px]">Total {advertisementData?.advertisementDeposit + numberOfDays(dateRange.from!, dateRange.to!) * advertisementData?.advertisementPrice}€</p>
                      }
                    </div>
                    {!currentUserIsOwner &&
                      <Button text="Payment" icon={true} long={false} onClick={proceedToPayment} />
                    }
                  </div>
                </div>
              </>
            )
          } />

          <StepItem step={2} setCurrentStep={setCurrentStep} currentStep={currentStep} StepContent={
            () => {

              const [creditCard, setCreditCard] = useState({
                cardholderName: '',
                cardNumber: '',
                expiryMonth: '',
                expiryYear: '',
                cvv: '',
                cvvLabel: 'CVC' as const
              })
              const reservationStart = dateRange.from?.toLocaleDateString("hr-RH")
              const reservationEnd = dateRange.to?.toLocaleDateString("hr-HR")

              return (
                <>
                  <div className="step-content">
                    <h1 className="text-white text-xl sm:text-2xl font-medium mb-8">Payment</h1>
                    <div className="flex flex-col gap-1 mb-8">
                      <h1 className="font-inter text-md font-medium text-white">{advertisementData?.itemName}</h1>
                      <p className="text-white">Total {advertisementData?.advertisementDeposit + numberOfDays(dateRange.from!, dateRange.to!) * advertisementData?.advertisementPrice}€</p>
                      <h3 className="text-white">{reservationStart} - {reservationEnd}</h3>
                    </div>

                    <CreditCard
                      value={creditCard}
                      onChange={setCreditCard}
                      processPayment={processPayment}
                      backOnClick={() => { setCurrentStep(1) }}
                    />
                  </div>
                </>
              )
            }} />



        </div>
        {
          errors.length !== 0 &&
          <PopupAlert errors={errors} />
        }

      </section>
    </section>
  );
}

export default Advertisement;
