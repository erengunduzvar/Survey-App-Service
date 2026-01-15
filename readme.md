# Survey Management Service

Bu servis, anket verilerinin yönetilmesi ve optimize edilmiş veri transfer modelleri (**DTO**) ile sunulması amacıyla geliştirilmiş bir Spring Boot backend uygulamasıdır.

## 🛠 Kullanılan Teknolojiler

* **Java 17+**: Veri modelleri ve DTO'lar için modern `record` yapıları kullanılmıştır.
* **Spring Boot 3.x**: Uygulama iskeleti ve REST API yönetimi.
* **Spring Data JPA**: Veritabanı katmanı ve repository yönetimi.
* **Java Streams**: Koleksiyonların işlenmesi ve manuel mapping süreçleri.
* **Enum Veri Tipleri**: Durum yönetimi için tip güvenliği sağlanmıştır.

## 📋 Öne Çıkan Özellikler

### 1. Optimize Edilmiş Veri Transferi (DTO Mapping)

Performans gereksinimleri doğrultusunda, anket listeleme işlemlerinde yüksek maliyetli ve ağır veri yükü oluşturan `sections` (bölümler) alanı hariç tutulmuştur. Bu sayede liste ekranları için gereken veri yükü minimize edilmiştir.

**Kapsanan Alanlar:**

* `surveyId`, `name`, `status` (Enum), `startDate`, `endDate`, `usersToSend`.

### 2. Immutable Data Models (Java Records)

Projeye özgü tüm veri taşıma objeleri, değiştirilemez (immutable) yapıda olan Java **Record**'ları ile tanımlanarak veri bütünlüğü korunmuştur.

```java
public record SurveyDto(
        String surveyId,
        String name,
        SurveyStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> usersToSend,
        List<SectionDto> sections
) {}

```

## ⚙️ Servis Yapısı ve Mantığı

### Veri Listeleme Stratejisi

`findAll()` metodunda tüm anketler çekilirken, belleği verimli kullanmak adına Stream API kullanılarak manuel mapping uygulanır. Bu aşamada `sections` alanı bilerek eşlenmez:

```java
@Transactional(readOnly = true)
public List<SurveyDto> findAll() {
    return surveyRepository.findAll().stream()
            .map(SurveyDto::mapToDtoWithoutSections) // Sadece gerekli alanları eşler
            .toList();
}

```

## ⚠️ Uygulama ve Tasarım Notları

* **Tip Güvenliği (Enum):** `SurveyStatus` alanı `String` yerine `Enum` olarak yönetilmektedir. Bu sayede geçersiz durum girişleri derleme ve çalışma zamanında engellenir.
* **Performans Odaklı Yaklaşım:** İlişkili tabloların (Sections) her liste sorgusunda (`findAll`) yüklenmemesi sağlanarak veritabanı ve network maliyeti düşürülmüştür.
* **Modüler DTO Kullanımı:** Gereksinimlere göre aynı Record üzerinden farklı mapping metodları (`mapToDto` vs `mapToDtoWithoutSections`) kullanılarak esneklik sağlanmıştır.

---