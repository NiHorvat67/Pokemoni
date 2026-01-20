import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}


export function parseReservationData(reservationData: any, accountRole: string) {
  // account role - ako je trader znaci da mu trebaju imena buyera, ako je buyer znaci da ce mu u history biti imena tradera
  const traderId = reservationData.advertisement.trader.accountId
  const traderName = reservationData.advertisement.trader.userFirstName + " " + reservationData.advertisement.trader.userLastName
  const buyerId = reservationData.buyer?.accountId
  const buyerName = reservationData.buyer?.userFirstName + " " + reservationData.buyer?.userLastName
  const advertisementId = reservationData.advertisement.advertisementId
  const productName = reservationData.advertisement.itemName
  const reservationStart = new Date(reservationData.reservation.reservationStart).toLocaleDateString("hr-RH")
  const reservationEnd = new Date(reservationData.reservation.reservationEnd).toLocaleDateString("hr-HR")
  const status = reservationData.status === "NOT ACTIVE" ? "not active" : "active"

  return {
    userId: accountRole === "buyer" ? traderId : buyerId,
    currentUserId: accountRole === "buyer" ? reservationData.reservation.buyerId : traderId,
    userName: accountRole === "buyer" ? traderName : buyerName,
    userRole: accountRole === "buyer" ? "trader" : "buyer",
    advertisementId,
    productName,
    reservationStart,
    reservationEnd,
    status
  }
}


export function numberOfDays(from: Date, to: Date) {
  if (from == undefined || to == undefined) {
    return 0;
  }
  const MS_PER_DAY = 1000 * 60 * 60 * 24
  return Math.round((to.getTime() - from.getTime()) / MS_PER_DAY) + 1
}
