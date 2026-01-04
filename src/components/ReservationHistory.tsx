import { DropdownMenuDialog } from "./DropdownMenuDialog";
import { MoreHorizontalIcon } from "lucide-react";
import { Button } from "./ui/button";
import { parseReservationData } from "@/lib/utils";


const ReservationHistory = ({ reservationsData, accountRole }: { reservationsData: any, accountRole: string }) => {
  return (
    <>
      <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[44px] mt-[54px]">Your reservations</h1>

      <div className="md:hidden flex flex-row flex-wrap gap-x-4 gap-y-4">
        {reservationsData?.map((reservationData: any) => {
          const {
            userId,
            userName,
            userRole,
            advertisementId,
            productName,
            reservationStart,
            reservationEnd,
            status } = parseReservationData(reservationData, accountRole)
          return (
            <div key={reservationData.reservation.reservationId} className="bg-[#222423] px-6.5 py-3.5 rounded-[8px] text-white min-w-[6rem] w-full">

              <div className="flex items-center justify-between mb-4">

                <a href={`/advertisement/${advertisementId}`} className="mr-8 max-w-[100ch] max-h-[6ch] text-nowrapx overflow-hidden text-ellipsis text-white font-medium text-base">{productName}</a>
                {status === "not active" &&
                  <div className="text-white text-base">
                    <DropdownMenuDialog />
                  </div>
                }
                {status === "active" &&
                  <div className="opacity-0">
                    <Button variant="transparent" className="hover:bg-[#444945] cursor-pointer" aria-label="Open menu" size="icon-sm">
                      <MoreHorizontalIcon />
                    </Button>
                  </div>
                }
              </div>

              <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                <h3>Status</h3>
                <h3>{status}</h3>
              </div>
              <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                <h3 className="capitalize">{userRole}</h3>
                <a href={`/profile/${userId}`} className="break-words">{userName}</a>
              </div>
              <div className="grid grid-cols-2 border-b-[1px] border-[#3A403D] py-[8px]">
                <h3>Date Start</h3>
                <h3>{reservationStart}</h3>
              </div>
              <div className="grid grid-cols-2 py-[8px]">
                <h3>Date End</h3>
                <h3>{reservationEnd}</h3>
              </div>

            </div>
          )
        }
        )}
      </div>

      <div className="max-md:hidden overflow-x-auto">
        <table className="font-inter text-white border-separate border-spacing-x-0 border-spacing-y-2 w-full text-nowrap">
          <thead>

            <tr className="">
              <th className="text-left pl-6 pr-2 font-normal">Product</th>
              <th className="text-left px-6 font-normal">{accountRole === "buyer" ? "Trader" : "Buyer"}</th>
              <th className="text-left px-6 font-normal">Status</th>
              <th className="text-left px-6 font-normal">Date</th>
            </tr>
          </thead>
          {/* provjeriti jeli trenutni korisnik i vlasnik profila */}
          <tbody>
            {reservationsData?.map((reservationData: any,) => {
              const {
                userId,
                userName,
                userRole,
                advertisementId,
                productName,
                reservationStart,
                reservationEnd,
                status } = parseReservationData(reservationData, accountRole)
              return (
                <tr key={reservationData.reservation.reservationId}>
                  <td className="bg-[#222423] pl-6 pr-2 py-2.5 rounded-l-[8px] max-w-[300px] overflow-hidden text-ellipsis">
                    <a href={`/advertisement/${advertisementId}`} className="text-white font-medium text-base">{productName}</a>
                  </td>

                  <td className="bg-[#222423] px-6 max-w-[220px] overflow-hidden text-ellipsis">
                    <a href={`/profile/${userId}`} className="text-white text-base">{userName}</a>
                  </td>

                  <td className="bg-[#222423] px-6">
                    <h3 className="text-desc text-base">{status}</h3>
                  </td>

                  <td className="bg-[#222423] px-6">
                    <h3 className="text-desc text-base">{reservationStart} - {reservationEnd}</h3>
                  </td>

                  <td className="bg-[#222423] px-6 rounded-r-[8px]">
                    {status === "not active" &&
                      <div className="justify-self-end">
                        <div className="text-white text-base">
                          <DropdownMenuDialog />
                        </div>
                      </div>
                    }
                    {status === "active" &&
                      <div className="justify-self-end opacity-0">
                        <div>
                          <Button variant="transparent" className="hover:bg-[#444945] cursor-pointer" aria-label="Open menu" size="icon-sm">
                            <MoreHorizontalIcon />
                          </Button>
                        </div>
                      </div>
                    }
                  </td>
                </tr>
              )
            }
            )}
          </tbody>

        </table>
      </div>
    </>
  );
}

export default ReservationHistory;
