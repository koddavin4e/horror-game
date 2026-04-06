import java.util.Scanner;

public class litme {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        System.out.println("------ Welcome to the Game! -----");
        System.out.println("Nigt echo - prolog");
        System.out.println("----------------------------------");
        System.out.println();
        System.out.println("ты просыпаешься в темной комнате, не помнишь, как сюда попал. Вокруг тебя только тишина и темнота.");
        System.out.println("старый деревянный дом скрипит, а за дверью слышится странный шум. ");
        System.out.println();

        firstScene();
    }

    public static void firstScene() {
        System.out.println("Лира: ...где я?");
        System.out.println("Лира: почему здесь так холодно?");
        System.out.println();
        System.out.println("Что сделать?");
        System.out.println("1. Осмотреть комнату");
        System.out.println("2. Подойти к двери");
        System.out.println("3. Закрыть глаза и притвориться, что ничего не происходит");

        int choice = sc.nextInt();
        sc.nextLine(); 

        if (choice == 1) {
            inspectRoom();
        } else if (choice == 2) {
            goToDoor();
        } else if (choice == 3) {
            stayStill();
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
            firstScene();
        }

    }

    public static void inspectRoom() {
        System.out.println("Ты осматриваешь комнату и видишь старый стол, покрытый пылью, и разбитое зеркало на стене.");
        System.out.println("Под окном ты видишь шкаф.");
        System.out.println("На полу ты замечаешь маленький ключ.");
        System.out.println();
        System.out.println("Лира: Ключ?... Может, он отсюда?");
        System.out.println();
        System.out.println("1. Взять ключ");
        System.out.println("2. Заглянуть в шкаф");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.println();
            System.out.println("Ты берешь ключ и идешь к двери. ");
            lockedDoorWithKey();
        } else if (choice == 2) {
            closetScene();
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
            inspectRoom();
        }
        
    }

    public static void goToDoor() {
        System.out.println("Ты подходишь к двери и слышишь странный шум за ней.");
        System.out.println("Дверь заперта, и ты не можешь открыть ее.");
        System.out.println();
        System.out.println("Лира: Заперто...Черт.");
        System.out.println();
        System.out.println("1. Осмотреть комнату");
        System.out.println("2. Постучать в дверь");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            inspectRoom();
        } else if (choice == 2) {
            System.out.println();
            System.out.println("Ты стучишь в дверь, и шум за ней становится громче.");
            System.out.println("Вдруг дверь открывается, и ты видишь темную фигуру, стоящую в проеме.");
            System.out.println("Фигура говорит: 'Ты не должен был сюда приходить...'");
            System.out.println();
            System.out.println("Лира: Кто ты? Что ты хочешь от меня?");
            System.out.println();
            System.out.println("1. Спрятаться");
            System.out.println("2. Поговорить с фигурой");

            int choice2 = sc.nextInt();
            sc.nextLine();

            if (choice2 == 1) {
                inspectRoom();
            } else if (choice2 == 2) {
                System.out.println();
                System.out.println("Ты стучишь в дверь.");
                System.out.println("За дверью становится тише.");
                System.out.println("А потом ты слышишь тихий голос, который говорит");
                System.out.println("'Не открывай...'");
                System.out.println("Лира: Кто здечь?");
                System.out.println();
                firstScene();
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
                goToDoor();
            }
        }
    }

    public static void stayStill() {
        System.out.println("Ты замираешь и стараешься не дышать");
        System.out.println("Шаги за дверью становятся ближе");
        System.out.println("Ты слышишь, как дкто то медленно проводит ногтем по двери");
        System.out.println();
        System.out.println("Лира: Черт... Они уже здесь...");
        System.out.println();
        System.out.println("1. Осмотреть комнату");
        System.out.println("2. Подойти к двери");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            inspectRoom();
        } else if (choice == 2) {
            goToDoor();
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
            stayStill();
        }
    }

    public static void closetScene() {
        System.out.println("Ты открываешь шкаф и видишь старую одежду и коробку с надписью ....");
        System.out.println("Ты открываешь коробку и находишь внутри записку.");
        System.out.println("На записке написано: 'Если ты слышишь шепот - не отвечай.'");
        System.out.println();
        System.out.println("Лира: Шепот? Что это может значить?");
        System.out.println();
        System.out.println("1. Взять записку и идти к двери");
        System.out.println("2. Вернуться назад ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.println();
            System.out.println("Ты берешь записку и идешь к двери.");
            goToDoor();
        } else if (choice == 2) {
            firstScene();
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
            closetScene();
        }
     
    }

    public static void lockedDoorWithKey() {
        System.out.println("Ты используешь ключ, чтобы открыть дверь.");
        System.out.println("Слышится тихий щелчок, и дверь медленно открывается.");
        System.out.println();
        System.out.println("Лира: Получилось... Но что там?");
        System.out.println("Дверь открывается, и ты видишь длинный коридор, освещенный тусклым светом.");
        System.out.println("Доносится детская мелодия и резкий детский смех.");
        System.out.println();
        System.out.println("Конец демо-версии. Спасибо за игру!");
        
    }
}
