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

const Advertisement = () => {
  const navigate = useNavigate()
  const { advertisementId } = useParams()
  const [currentUserIsOwner, setCurrentUserIsOwner] = useState<boolean>(false)
  const [dateRange, setDateRange] = useState<{ from: Date | undefined, to: Date | undefined }>({ from: undefined, to: undefined })
  const { user } = useAuthContext()
  const [errors, setErrors] = useState<any>([])

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
          console.log(res.data)
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
          return res.data
        })
        .catch(err => console.log(err));
    },
    onSuccess: () => {
      navigate("/")
    }
  });


  function rentOnClick() {
    setErrors([])
    if (datesSelected()) {
      makeReservation()
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
        <div className="flex gap-8 lg:gap-20 max-lg:flex-col">
          <img src={`/ api / advertisements / images / load / ${advertisementData?.advertisementId} `} alt="product image" className="lg:w-2/5 object-cover object-center rounded-[8px] w-full max-h-[300px] lg:max-h-[400px]" />
          <div className="flex flex-col items-start gap-10 lg:gap-12 flex-1">
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

            <div className="flex font-inter items-center gap-11">
              <div className="flex flex-col items-start flex-1 justify-start text-white min-w-[100px]">
                <h1 className="font-medium text-[32px]">{advertisementData?.advertisementPrice}€</h1>
                <p className="text-desc text-sm">Kaucija {advertisementData?.advertisementDeposit}€</p>
              </div>
              {!currentUserIsOwner &&
                <Button text="Rent" icon={true} long={false} onClick={rentOnClick} />
              }
            </div>
            {reservationPeriods !== undefined &&
              <Calendar14 range={dateRange}
                setRange={setDateRange}
                advertisementStart={advertisementData?.advertisementStart}
                advertisementEnd={advertisementData?.advertisementEnd}
                reservationPeriods={reservationPeriods}
              />
            }

          </div>
        </div>

      </section>
      {errors.length !== 0 &&
        <PopupAlert errors={errors} />
      }
    </section>
  );
}

export default Advertisement;
